package ru.npv.exam.jc.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.jc.app.service.utils.config.ExamConfig;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.app.exception.ProcessAlreadyFinished;
import ru.npv.exam.jc.app.domain.app.CheckAnswerService;
import ru.npv.exam.jc.app.domain.app.ExamProcess;
import ru.npv.exam.jc.app.service.utils.QuestionInputValidator;

import java.io.*;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;

public class ConsoleExamProcess implements ExamProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleExamProcess.class);

    private final CheckAnswerService checkAnswerService;
    private final InputStream consoleInput;
    private final OutputStream consoleOutput;
    private final List<AbstractQuestion> questionsSet;
    private final Random random = new Random(System.currentTimeMillis());
    private final int MAX_ATTEMPTS = 5;     // Количество попыток ввода
    private final int passingPercent;
    private final ExamConfig config;

    private Scanner processInput;
    private PrintStream processOutput;
    private boolean finished;
    private Map<String, String> parameters;

    public ConsoleExamProcess(CheckAnswerService checkAnswerService, InputStream consoleInput, OutputStream consoleOutput, ExamConfig examConfig, ExamProcess preProcess, List<AbstractQuestion> questionsSet) {
        this.checkAnswerService = checkAnswerService;
        this.consoleInput = consoleInput;
        this.consoleOutput = consoleOutput;
        this.questionsSet = questionsSet;
        this.config = examConfig;
        this.finished = false;
        if (config.getPassingPercent() > 100) {
            this.passingPercent = 100;
        } else if (config.getPassingPercent() < 1) {
            this.passingPercent = 1;
        } else {
            this.passingPercent = config.getPassingPercent();
        }
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
            for (questionsCount = 0; questionsCount < config.getMaxQuestionsCount(); questionsCount++) {
                boolean checkResult = askQuestion(questionsCount);
                rightAnswers += checkResult ? 1 : 0;
                if (config.getNeedResults() && checkResult) {
                    processOutput.println(config.getTrueMessage());
                }
                if (config.getNeedResults() && !checkResult) {
                    processOutput.println(config.getFalseMessage());
                }
                processOutput.println();
            }
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
        } catch (NeedExitException e) {
            sayGoodbye(config.getMessageUserBreak(), userFullName);
            LOG.debug("", e);
        } catch (QuestionsOverException e) {
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
            LOG.debug("", e);
        } catch (Exception e) {
            sayGoodbye(config.getMessageTechError(), userFullName);
            LOG.error("Technical error", e);
        } finally {
            finished = true;
        }
    }

    private void initProcess() {
        if (finished) {
            throw new ProcessAlreadyFinished(config.getMessageProcessCompleted());
        }
        processInput = new Scanner(new InputStreamReader(consoleInput));
        if (consoleOutput instanceof PrintStream) {
            processOutput = (PrintStream) consoleOutput;
        } else {
            processOutput = new PrintStream(consoleOutput);
        }
    }

    private String promptUserFullName() throws NeedExitException {
        processOutput.println(config.getIntroPrompt());
        processOutput.print(config.getPromptName()+" ");
        String firstName = promptWithoutValidation();
        processOutput.print(config.getPromptSurname()+" ");
        String lastName = promptWithoutValidation();
        processOutput.print(config.getPromptMiddle()+" ");
        String middleName = promptWithoutValidation();
        processOutput.println();
        return (isEmpty(lastName) ? "" : lastName + " ") + (isEmpty(firstName) ? "" : firstName + " ") + (isEmpty(middleName) ? "" : middleName + " ");
    }

    private String checkExitInput(String input) throws NeedExitException {
        LOG.trace(">> input [{}]", input);
        if (!isEmpty(input) && config.getExitInput().equals(input.trim().toLowerCase())) {
            throw new NeedExitException(String.format(config.getErrorUserExit(), config.getExitInput()));
        }
        return input;
    }

    private void helloMessage() {
        processOutput.println(config.getCustomHelloMessage());
        processOutput.println(String.format(config.getExitPromptMessage(), config.getExitInput()));
    }

    private void greetingMessage(String fullName) {
        processOutput.println(String.format(config.getGreetingMessage(), (isEmpty(fullName) ? "" : ", " + fullName)));
    }

    private void sayGoodbye(String resultMessage, String fullName) {
        if (isEmpty(resultMessage)) {
            processOutput.println(config.getTestFailedMessage());
        } else {
            processOutput.println(resultMessage);
        }
        processOutput.println(String.format(config.getGoodbyeMessage()+"\n", (isEmpty(fullName) ? "" : ", " + fullName)));
    }

    private boolean askQuestion(int questionsCount) throws NeedExitException, QuestionsOverException {
        AbstractQuestion question = nextQuestion();
        processOutput.println(String.format(config.getQuestionTemplate(), questionsCount + 1, question.getText()));
        String answer;
        switch (question.getType()) {
            case CLOSE_ENDED:
            case YES_NO:
                printVariants(question);
            case OPEN_ENDED:
                break;
            default:
                throw new IllegalArgumentException(config.getErrorUndefinedQuestion());
        }
        processOutput.print(config.getAnswerTitle()+" ");
        answer = promptWithValidation(question);
        LOG.trace("Question:{} Type:{} Answer:{}", question.getText(), question.getType(), answer);
        return !isEmpty(answer) && checkAnswerService.check(question, answer);
    }

    private void printVariants(AbstractQuestion question) {
        List<String> variants = question.getVariants();
        if (variants.isEmpty()) {
            return;
        }
        for (int i = 1; i <= variants.size(); i++) {
            processOutput.println(String.format(config.getVariantTemplate(), i, variants.get(i - 1)));
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
                processOutput.println(String.format(config.getAttemptsMessage(), i));
                processOutput.print(config.getAnswerTitle()+" ");
            }
        }
        processOutput.println(config.getQuestionSkipped());
        return null;
    }

    private AbstractQuestion nextQuestion() throws QuestionsOverException {
        if (questionsSet.isEmpty()) {
            throw new QuestionsOverException(config.getErrorQuestionsEnds());
        }
        int nextIndex = random.nextInt(questionsSet.size());
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    private String getResultMessage(int count, int rightAnswers) {
        int percent = (int) ((double) rightAnswers / (double) count * 100);
        LOG.trace("rigths: {}, count: {}, passing %: {}, persent: {}", rightAnswers, count, passingPercent, percent);
        String result = percent >= passingPercent ? config.getResultSuccess() : config.getResultFail();
        return String.format(config.getCustomResultMessage(), count, rightAnswers, percent, result);
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
