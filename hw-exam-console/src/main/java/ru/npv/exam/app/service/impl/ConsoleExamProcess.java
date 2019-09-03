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
    private final int MAX_ATTEMPTS = 5;     // Попытки валидного ввода
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
        sayHello();
        try {
            userFullName = promptUserFullName();
            for (questionsCount=0; questionsCount < maxQuestionsCount; questionsCount++) {
                boolean checkResult = askQuestion(questionsCount);
                rightAnswers += checkResult ? 1 : 0;
                if (needResults && checkResult) {
                    processOutput.println(trueMessage);
                }
                if (needResults && !checkResult) {
                    processOutput.println(falseMessage);
                }
            }
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
        } catch (NeedExitException e) {
            sayGoodbye("Вы прервали тест.", userFullName);
            LOG.debug("", e);
        } catch (QuestionsOverException e) {
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
            LOG.debug("", e);
        }
        catch (Exception e) {
            sayGoodbye("В процессе выполнения произошла ошибка.", userFullName);
            LOG.error("Ошибка выплонения", e);
        } finally {
            finished = true;
        }
    }

    private void initProcess() {
        if (finished) {
            throw new ProcessAlreadyFinished("Процесс уже завершён.");
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

        processInput = new Scanner(consoleInput);
        if (consoleOutput instanceof PrintStream) {
            processOutput = (PrintStream) consoleOutput;
        } else {
            processOutput = new PrintStream(consoleOutput);
        }
    }

    private String promptUserFullName() throws NeedExitException {
        return "Кремер Яков Иосифович";
    }

    private String checkExitInput(String input) throws NeedExitException {
        LOG.debug("input from {}", input);
        if (!isEmpty(input) && "--exit".equals(input.trim().toLowerCase())) {
            throw new NeedExitException("Пользователь ввёл --exit");
        }
        LOG.debug("input to {}", input);
        return input;
    }

    private void sayHello() {
        processOutput.println(customHelloMessage);
        processOutput.println("Для прерывания теста введите --exit вместо любого ответа.");
    }

    private void sayGoodbye(String resultMessage, String fullName) {
        if (isEmpty(resultMessage)) {
            processOutput.println("Тест не пойден.");
        } else {
            processOutput.println(resultMessage);
        }
        processOutput.println("До свидания" + (isEmpty(fullName) ? "." : ", " + fullName));
    }

    private boolean askQuestion(int questionsCount) throws NeedExitException, QuestionsOverException {
        AbstractQuestion question = nextQuestion();
        processOutput.println(String.format("Вопрос %d: %s", questionsCount+1, question.getText()));
        String answer;
        switch (question.getType()) {
            case CLOSE_ENDED:
            case YES_NO:
                printVariants(question);
            case OPEN_ENDED:
                break;
            default:
                throw new IllegalArgumentException("Тип вопроса не определён.");
        }
        processOutput.print("Ответ: ");
        answer = promptWithValidation(question);
        LOG.debug("Вопрос:{} Тип:{} Ответ:{}", question.getText(), question.getType(), answer);
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

    private <T extends AbstractQuestion> String promptWithValidation(T question) throws NeedExitException {
        for (int i = MAX_ATTEMPTS; i > 0; i--) {
            String input = checkExitInput(processInput.nextLine()).trim();
            if (QuestionInputValidator.validate(question, input)) {
                return input;
            } else {
                processOutput.println("Введено некорректное значение. Осталось попыток: "+i);
                processOutput.print("Ответ: ");
            }
        }
        return null;
    }

    private AbstractQuestion nextQuestion() throws QuestionsOverException {
        if (questionsSet.isEmpty()) {
            throw new QuestionsOverException("Вопросы закончились.");
        }
        int nextIndex = random.nextInt(questionsSet.size());
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    private String getResultMessage(int count, int rightAnswers) {
        int percent = (int)((double) rightAnswers / (double) count * 100);
        LOG.debug("rigths: {}, count: {}, passing %: {}, persent: {}", rightAnswers, count, passingPercent, percent);
        String result = percent >= passingPercent ? "пройден" : "не пройден";
        if (!isEmpty(customResultMessage)) {
            return String.format(customResultMessage, count, rightAnswers, percent, result);
        }
        return String.format("Вопросов: %d. Верных ответов: %d (%d%%). Тест %s.", count, rightAnswers, percent, result);
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
