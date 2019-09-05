package ru.npv.exam.jc.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.app.exception.ProcessAlreadyFinished;
import ru.npv.exam.jc.app.domain.app.CheckAnswerService;
import ru.npv.exam.jc.app.domain.app.ExamProcess;
import ru.npv.exam.jc.app.domain.app.utils.QuestionInputValidator;

import java.io.*;
import java.util.*;

import static org.springframework.util.StringUtils.isEmpty;
import static ru.npv.exam.jc.app.domain.app.utils.Constants.PROPERTY_MAX_QUESTIONS;

public class ConsoleExamProcess implements ExamProcess {
    private static final Logger LOG = LoggerFactory.getLogger(ConsoleExamProcess.class);

    private final CheckAnswerService checkAnswerService;
    private final InputStream consoleInput;
    private final OutputStream consoleOutput;
    private final List<AbstractQuestion> questionsSet;
    private final Random random = new Random(System.currentTimeMillis());
    private final int MAX_ATTEMPTS = 5;     // Попытки валидного ввода

    @Value("${"+PROPERTY_MAX_QUESTIONS+"}")
    private Integer maxQuestionsCount = 6;

    //TODO: вынести всё в props
    @Value("${begin.message}")
    private String customHelloMessage;
    @Value("${result.message}")
    private String customResultMessage = "Вопросов: %d. Верных ответов: %d (%d%%). Тест %s.";
    @Value("${check.answer.true}")
    private String trueMessage = "Верно";
    @Value("${check.answer.false}")
    private String falseMessage = "Неверно";
    @Value("${all.check.results}")
    private Boolean needResults = false;
    @Value("${result.passing.percent}")
    private Integer passingPercent = 85;
    @Value("${exit.input}")
    private String exitInput = "release me";
    @Value("${message.user.break}")
    private String messageUserBreak;
    @Value("${message.tech.error}")
    private String messageTechError;
    @Value("${message.process.completed}")
    private String messageProcessCompleted;
    @Value("${prompt.introduce}")
    private String introPrompt;
    @Value("${prompt.firstname}")
    private String promptName;
    @Value("${prompt.middlename}")
    private String promptMiddle;
    @Value("${prompt.lastname}")
    private String promptSurname;
    @Value("${error.user.exit}")
    private String errorUserExit;
    @Value("${prompt.exit.message}")
    private String exitPromptMessage;
    @Value("${message.greeting}")
    private String greetingMessage;
    @Value("${message.test.failed}")
    private String testFailedMessage;
    @Value("${message.goodbye}")
    private String goodbyeMessage;
    @Value("${error.undefined.question.type}")
    private String errorUndefinedQuestion;
    @Value("${template.question}")
    private String questionTemplate = "Вопрос %d: %s";
    @Value("${template.variant}")
    private String variantTemplate = "%s (%d)";
    @Value("${message.attempts}")
    private String attemptsMessage;
    @Value("${message.answer.title}")
    private String answerTitle;
    @Value("message.question.skipped")
    private String questionSkipped;
    @Value("${error.questions.ends}")
    private String errorQuestionsEnds;
    @Value("${result.fail}")
    private String resultFail="success";
    @Value("${result.success}")
    private String resultSuccess="fail";

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
            for (questionsCount = 0; questionsCount < maxQuestionsCount; questionsCount++) {
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
            sayGoodbye(messageUserBreak, userFullName);
            LOG.debug("", e);
        } catch (QuestionsOverException e) {
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
            LOG.debug("", e);
        } catch (Exception e) {
            sayGoodbye(messageTechError, userFullName);
            LOG.error("Technical error", e);
        } finally {
            finished = true;
        }
    }

    private void initProcess() {
        if (finished) {
            throw new ProcessAlreadyFinished(messageProcessCompleted);
        }
        if (passingPercent > 100)
            passingPercent = 100;
        if (passingPercent < 1)
            passingPercent = 1;
        processInput = new Scanner(new InputStreamReader(consoleInput));
        if (consoleOutput instanceof PrintStream) {
            processOutput = (PrintStream) consoleOutput;
        } else {
            processOutput = new PrintStream(consoleOutput);
        }
    }

    private String promptUserFullName() throws NeedExitException {
        processOutput.println(introPrompt);
        processOutput.print(promptName+" ");
        String firstName = promptWithoutValidation();
        processOutput.print(promptSurname+" ");
        String lastName = promptWithoutValidation();
        processOutput.print(promptMiddle+" ");
        String middleName = promptWithoutValidation();
        processOutput.println();
        return (isEmpty(lastName) ? "" : lastName + " ") + (isEmpty(firstName) ? "" : firstName + " ") + (isEmpty(middleName) ? "" : middleName + " ");
    }

    private String checkExitInput(String input) throws NeedExitException {
        LOG.trace(">> input [{}]", input);
        if (!isEmpty(input) && exitInput.equals(input.trim().toLowerCase())) {
            throw new NeedExitException(String.format(errorUserExit,  exitInput));
        }
        return input;
    }

    private void helloMessage() {
        processOutput.println(customHelloMessage);
        processOutput.println(String.format(exitPromptMessage, exitInput));
    }

    private void greetingMessage(String fullName) {
        processOutput.println(String.format(greetingMessage, (isEmpty(fullName) ? "" : ", " + fullName)));
    }

    private void sayGoodbye(String resultMessage, String fullName) {
        if (isEmpty(resultMessage)) {
            processOutput.println(testFailedMessage);
        } else {
            processOutput.println(resultMessage);
        }
        processOutput.println(String.format(goodbyeMessage+"\n", (isEmpty(fullName) ? "" : ", " + fullName)));
    }

    private boolean askQuestion(int questionsCount) throws NeedExitException, QuestionsOverException {
        AbstractQuestion question = nextQuestion();
        processOutput.println(String.format(questionTemplate, questionsCount + 1, question.getText()));
        String answer;
        switch (question.getType()) {
            case CLOSE_ENDED:
            case YES_NO:
                printVariants(question);
            case OPEN_ENDED:
                break;
            default:
                throw new IllegalArgumentException(errorUndefinedQuestion);
        }
        processOutput.print(answerTitle+" ");
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
            processOutput.println(String.format(variantTemplate, i, variants.get(i - 1)));
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
                processOutput.println(String.format(attemptsMessage, i));
                processOutput.print(answerTitle+" ");
            }
        }
        processOutput.println(questionSkipped);
        return null;
    }

    private AbstractQuestion nextQuestion() throws QuestionsOverException {
        if (questionsSet.isEmpty()) {
            throw new QuestionsOverException(errorQuestionsEnds);
        }
        int nextIndex = random.nextInt(questionsSet.size());
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    private String getResultMessage(int count, int rightAnswers) {
        int percent = (int) ((double) rightAnswers / (double) count * 100);
        LOG.trace("rigths: {}, count: {}, passing %: {}, persent: {}", rightAnswers, count, passingPercent, percent);
        String result = percent >= passingPercent ? resultSuccess : resultFail;
        return String.format(customResultMessage, count, rightAnswers, percent, result);
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
