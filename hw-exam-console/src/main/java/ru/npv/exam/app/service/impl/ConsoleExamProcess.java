package ru.npv.exam.app.service.impl;

import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.service.CheckAnswerService;
import ru.npv.exam.app.service.ExamProcess;

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
    private int maxQuestionsCount;
    private int questionsCount;
    private String helloMessage;
    private String resultMessage;
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
        String resultMessage = null;
        try {
            userFullName = promptUserFullName();



            sayGoodbye(resultMessage, userFullName);
        } catch (NeedExitException e) {
            sayGoodbye("Вы прервали тест.", userFullName);
        } catch (Exception e) {
            sayGoodbye("В процессе выполнения произошла ошибка.", userFullName);
        }
    }

    private void initProcess() {
        questionsCount = 0;
        maxQuestionsCount = 5;
        try {
            maxQuestionsCount = Integer.valueOf(parameters.get(PROPERTY_MAX_QUESTIONS));
        } catch (Exception e) {}
        helloMessage = parameters.get(PROPERTY_BEGIN_MESSAGE);
        resultMessage = parameters.get(PROPERTY_RESULT_MESSAGE);
        needResults = Boolean.FALSE;
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
        if (!isEmpty(input) && "EXIT".equals(input.trim().toUpperCase())) {
            throw new NeedExitException("Пользователь ввёл exit");
        }
        return input;
    }

    private void sayGoodbye(String resultMessage, String fullName) {
        if (isEmpty(resultMessage)) {
            processOutput.println("Тест не пойден.");
        } else {
            processOutput.println(resultMessage);
        }
        processOutput.println("До свидания" + (isEmpty(fullName) ? "." : ", " + fullName));
    }

    private void askQuestion() throws NeedExitException {

    }

    private AbstractQuestion nextQuestion() {
        int nextIndex = random.nextInt(questionsSet.size()-1);
        AbstractQuestion question = questionsSet.get(nextIndex);
        questionsSet.remove(nextIndex);
        return question;
    }

    class NeedExitException extends Exception {
        public NeedExitException(String message) {
            super(message);
        }
    }
}
