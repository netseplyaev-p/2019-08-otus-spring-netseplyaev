package ru.npv.exam.app.service.impl;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.exception.ProcessAlreadyFinished;
import ru.npv.exam.app.service.CheckAnswerService;
import ru.npv.exam.app.service.ExamProcess;
import ru.npv.exam.app.service.utils.QuestionInputValidator;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import static ru.npv.exam.app.service.utils.Constants.*;
import static org.springframework.util.StringUtils.isEmpty;

public class ConsoleExamProcess implements ExamProcess {

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
            for (questionsCount=1; questionsCount <= maxQuestionsCount; questionsCount++) {
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
        } catch (QuestionsOverException e) {
            sayGoodbye(getResultMessage(questionsCount, rightAnswers), userFullName);
        }
        catch (Exception e) {
            sayGoodbye("В процессе выполнения произошла ошибка.", userFullName);
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
        customHelloMessage = parameters.get(PROPERTY_BEGIN_MESSAGE);
        customResultMessage = parameters.get(PROPERTY_RESULT_MESSAGE);
        needResults = false;
        try {
            needResults = Boolean.valueOf(parameters.get(PROPERTY_ALL_CHECK_RESULTS));
        } catch (Exception e) {}
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
        if (!isEmpty(input) && "--exit".equals(input.trim().toLowerCase())) {
            throw new NeedExitException("Пользователь ввёл --exit");
        }
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
        processOutput.println(String.format("Вопрос %d: %s", questionsCount, question.getText()));
        processOutput.print("Ответ: ");
        String answer;
        switch (question.getType()) {
            case OPEN_ENDED:
                answer = promptWithValidation(question);
                return !isEmpty(answer) && checkAnswerService.check(question, answer);
            case CLOSE_ENDED:
            case YES_NO:
                printVariants(question);
                answer = promptWithValidation(question);
                return !isEmpty(answer) && checkAnswerService.check(question, answer);
            default:
                throw new IllegalArgumentException("Тип вопроса не определён.");
        }
    }

    private void printVariants(AbstractQuestion question) {
        List<String> variants = question.getVariants();
        if (variants.isEmpty()) {
            return;
        }
        for (int i=1; i <= variants.size(); i++) {
            processOutput.println(String.format("%d. %s", i, variants.get(i)));
        }
    }

    private <T extends AbstractQuestion> String promptWithValidation(T question) throws NeedExitException {
        for (int i = MAX_ATTEMPTS; i > 0; i--) {
            String input = checkExitInput(processInput.nextLine()).trim();
            if (QuestionInputValidator.validate(question, input)) {
                return input;
            } else {
                processOutput.println("Введено некорректное значение. Осталось попыток: "+i);
            }
        }
        return null;
    }

    private AbstractQuestion nextQuestion() throws QuestionsOverException {
        if (questionsSet.isEmpty()) {
            throw new QuestionsOverException("Вопросы закончились.");
        }
        int nextIndex = random.nextInt(questionsSet.size()-1);
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    private String getResultMessage(int count, int rightAnswers) {
        if (!isEmpty(customResultMessage)) {
            return String.format(customHelloMessage, count, rightAnswers);
        }
        return String.format("Вопросов: %d. Верных ответов: %d. Тест %s", count, rightAnswers);
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
