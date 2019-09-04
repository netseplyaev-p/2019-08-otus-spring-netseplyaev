package ru.npv.exam.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.exception.ProcessAlreadyFinished;
import ru.npv.exam.app.service.CheckAnswerService;
import ru.npv.exam.app.service.ExamProcess;
import ru.npv.exam.app.service.utils.QuestionInputValidator;

import java.io.*;
import java.util.*;

import static ru.npv.exam.app.service.utils.Constants.*;
import static org.springframework.util.StringUtils.isEmpty;

public class ConsoleExamProcess implements ExamProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleExamProcess.class);

    private final CheckAnswerService checkAnswerService;
    private final InputStream consoleInput;
    private final OutputStream consoleOutput;
    private final List<AbstractQuestion> questionsSet;
    private final Random random = new Random(System.currentTimeMillis());
    private final int MAX_ATTEMPTS = 5;     // ������� ��������� �����
    private Integer maxQuestionsCount;
    private String customHelloMessage;
    private String customResultMessage;
    private String trueMessage;
    private String falseMessage;
    private Boolean needResults;
    private Integer passingPercent;

    private Scanner processInput;
    private PrintStream processOutput;
    private boolean finished;

    private Map<String, String> parameters;

    public ConsoleExamProcess(CheckAnswerService checkAnswerService, InputStream consoleInput, OutputStream consoleOutput, List<AbstractQuestion> questionsSet) {
        this.checkAnswerService = checkAnswerService;
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.questionsSet = questionsSet;
        finished = false;
    }

    @Override
    public void setParameters(Map<String, ?> parameters) {
        this.parameters = (Map<String, String>) parameters;
    }

    @Override
    public void run() {
        initProcess();
        String userFullName = null;
        int rightAnswers = 0;
        int questionsCount = 0;
        helloMessage();
        try {
            userFullName = promptUserFullName();
            greetingMessage(userFullName);
            for (questionsCount=0; questionsCount < maxQuestionsCount; questionsCount++) {
                boolean checkResult = askQuestion(questionsCount);
                rightAnswers += checkResult ? 1 : 0;
                if (needResults && checkResult) {
                    processOutput.println(trueMessage);
                }
                if (needResults && !checkResult) {
                    processOutput.println(falseMessage);
                }
                processOutput.println();
            }
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
        } catch (NeedExitException e) {
            sayGoodbye("�� �������� ����.", userFullName);
            LOG.debug("", e);
        } catch (QuestionsOverException e) {
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
            LOG.debug("", e);
        }
        catch (Exception e) {
            sayGoodbye("� �������� ���������� ��������� ������.", userFullName);
            LOG.error("������ ����������", e);
        } finally {
            finished = true;
        }
    }

    private void initProcess() {
        if (finished) {
            throw new ProcessAlreadyFinished("������� ��� ��������.");
        }
        maxQuestionsCount = 6;
        try {
            maxQuestionsCount = Integer.valueOf(parameters.get(PROPERTY_MAX_QUESTIONS));
        } catch (Exception e) {}
        needResults = false;
        try {
            needResults = Boolean.valueOf(parameters.get(PROPERTY_ALL_CHECK_RESULTS));
        } catch (Exception e) {}
        passingPercent = 70;
        try {
            passingPercent = Integer.valueOf(parameters.get(PROPERTY_PASSING_PERCENT));
            if (passingPercent > 100)
                passingPercent = 100;
            if (passingPercent < 1)
                passingPercent = 1;
        } catch (Exception e) {}
        customHelloMessage = parameters.get(PROPERTY_BEGIN_MESSAGE);
        customResultMessage = parameters.get(PROPERTY_RESULT_MESSAGE);

        trueMessage = parameters.get(PROPERTY_CHECK_TRUE);
        falseMessage = parameters.get(PROPERTY_CHECK_FALSE);

        processInput = new Scanner(new InputStreamReader(consoleInput));
        if (consoleOutput instanceof PrintStream) {
            processOutput = (PrintStream) consoleOutput;
        } else {
            processOutput = new PrintStream(consoleOutput);
        }
    }

    private String promptUserFullName() throws NeedExitException {
        processOutput.println("����������, �������������.");
        processOutput.print("���� ���: ");
        String firstName = promptWithoutValidation();
        processOutput.print("���� �������: ");
        String lastName = promptWithoutValidation();
        processOutput.print("���� ��������: ");
        String middleName = promptWithoutValidation();
        processOutput.println();
        return (isEmpty(lastName) ? "" : lastName + " ") + (isEmpty(firstName) ? "" : firstName + " ") + (isEmpty(middleName) ? "" : middleName + " ");
    }

    private String checkExitInput(String input) throws NeedExitException {
        LOG.trace(">> input [{}]", input);
        if (!isEmpty(input) && "--exit".equals(input.trim().toLowerCase())) {
            throw new NeedExitException("������������ ��� --exit");
        }
        return input;
    }

    private void helloMessage() {
        processOutput.println(customHelloMessage);
        processOutput.println("��� ���������� ����� ������� --exit ������ ������ ������.");
    }

    private void greetingMessage(String fullName) {
        processOutput.println("����� ����������" + (isEmpty(fullName) ? "" : ", "+fullName) + "! ");
    }

    private void sayGoodbye(String resultMessage, String fullName) {
        if (isEmpty(resultMessage)) {
            processOutput.println("���� �� ������.");
        } else {
            processOutput.println(resultMessage);
        }
        processOutput.println("�� ��������" + (isEmpty(fullName) ? "" : ", " + fullName) + "!\n");
    }

    private boolean askQuestion(int questionsCount) throws NeedExitException, QuestionsOverException {
        AbstractQuestion question = nextQuestion();
        processOutput.println(String.format("������ %d: %s", questionsCount+1, question.getText()));
        String answer;
        switch (question.getType()) {
            case CLOSE_ENDED:
            case YES_NO:
                printVariants(question);
            case OPEN_ENDED:
                break;
            default:
                throw new IllegalArgumentException("��� ������� �� ��������.");
        }
        processOutput.print("�����: ");
        answer = promptWithValidation(question);
        LOG.trace("������:{} ���:{} �����:{}", question.getText(), question.getType(), answer);
        return !isEmpty(answer) && checkAnswerService.check(question, answer);
    }

    private void printVariants(AbstractQuestion question) {
        List<String> variants = question.getVariants();
        if (variants.isEmpty()) {
            return;
        }
        for (int i=1; i <= variants.size(); i++) {
            processOutput.println(String.format("%d. %s", i, variants.get(i-1)));
        }
    }

    private String promptWithoutValidation() throws NeedExitException {
        String input = processInput.nextLine();
        LOG.trace("raw input [{}]", input);
        return checkExitInput(input).trim();
    }

    private <T extends AbstractQuestion> String promptWithValidation(T question) throws NeedExitException {
        for (int i = MAX_ATTEMPTS; i > 0; i--) {
            String raw_input = processInput.nextLine();
            LOG.trace("raw input [{}]", raw_input);
            String input = checkExitInput(raw_input).trim();
            if (QuestionInputValidator.validate(question, input)) {
                return input;
            } else {
                processOutput.println("������� ������������ ��������. �������� �������: "+i);
                processOutput.print("�����: ");
            }
        }
        return null;
    }

    private AbstractQuestion nextQuestion() throws QuestionsOverException {
        if (questionsSet.isEmpty()) {
            throw new QuestionsOverException("������� �����������.");
        }
        int nextIndex = random.nextInt(questionsSet.size());
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    private String getResultMessage(int count, int rightAnswers) {
        int percent = (int)((double) rightAnswers / (double) count * 100);
        LOG.trace("rigths: {}, count: {}, passing %: {}, persent: {}", rightAnswers, count, passingPercent, percent);
        String result = percent >= passingPercent ? "�������" : "�� �������";
        if (!isEmpty(customResultMessage)) {
            return String.format(customResultMessage, count, rightAnswers, percent, result);
        }
        return String.format("��������: %d. ������ �������: %d (%d%%). ���� %s.", count, rightAnswers, percent, result);
    }

    class NeedExitException extends Exception {
        public NeedExitException(String message) {
            super(message);
        }
    }

    class QuestionsOverException extends Exception {
        public QuestionsOverException(String message) {
            super(message);
        }
    }
}
