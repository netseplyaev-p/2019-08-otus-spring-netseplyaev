package ru.npv.exam.app.service.impl;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.service.LoadQuestionsService;
import ru.npv.exam.app.service.StringPropertiesService;
import ru.npv.exam.app.service.UserRequestService;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class SingleUserConsoleRequestService implements UserRequestService<InputStream, OutputStream> {
    private final Logger LOG = LoggerFactory.getLogger(SingleUserConsoleRequestService.class);

    private final LoadQuestionsService questionsService;
    private final StringPropertiesService propertiesService;

    private Process process;


    public SingleUserConsoleRequestService(LoadQuestionsService questionsService, StringPropertiesService propertiesService) {
        this.questionsService = questionsService;
        this.propertiesService = propertiesService;
    }

    @Override
    public void init(InputStream input, OutputStream output) {
        restartProcess(input, output);
    }

    private void restartProcess(InputStream input, OutputStream output) {
        process = new Process(questionsService.load(), input, output);

    }

    @Override
    public AbstractQuestion nextQuestion() {
        process.nextQuestion();
        return process.getCurrentQuestion();
    }

    @Override
    public boolean checkInput(String userInput) {
        return false;
    }

    class Process {
        private final List<AbstractQuestion> questions;
        private final List<Integer> availableIndexes;
        private final InputStream consoleInputStream;
        private final OutputStream consoleOutputStream;
        @Getter
        private AbstractQuestion currentQuestion;

        Process(List<AbstractQuestion> questions, InputStream consoleInputStream, OutputStream consoleOutputStream) {
            this.questions = questions;
            this.consoleInputStream = consoleInputStream;
            this.consoleOutputStream = consoleOutputStream;
            availableIndexes = new LinkedList<>();
            for (int i=0; i<=questions.size(); i++) {
                availableIndexes.add(i);
            }
        }

        public void nextQuestion() {

        }
    }
}
