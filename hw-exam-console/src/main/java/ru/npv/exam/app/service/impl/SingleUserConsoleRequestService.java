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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SingleUserConsoleRequestService implements UserRequestService<InputStream, OutputStream> {
    private static final Logger LOG = LoggerFactory.getLogger(SingleUserConsoleRequestService.class);
    private static final String PROPERTY_MAX_QUESTIONS = "max.questions";

    private final LoadQuestionsService questionsService;
    private final StringPropertiesService propertiesService;

    private Process process;
    private Integer maxQuestions;


    public SingleUserConsoleRequestService(LoadQuestionsService questionsService, StringPropertiesService propertiesService) {
        this.questionsService = questionsService;
        this.propertiesService = propertiesService;
        if (propertiesService != null) {
            try {
                maxQuestions = Integer.valueOf(propertiesService.getStringProperty(PROPERTY_MAX_QUESTIONS).get());
            } catch (Exception e) {
                LOG.debug("Свойство {} не получено", PROPERTY_MAX_QUESTIONS);
            }
        }
    }

    @Override
    public void init(InputStream input, OutputStream output) {
        restartProcess(input, output);
    }

    private void restartProcess(InputStream input, OutputStream output) {
        process = new Process(questionsService.load(), input, output, maxQuestions);
        process.start();
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
        private final Integer DEFAULT_MAX_QUESTIONS = 5;

        private final List<AbstractQuestion> questions;
        private final List<Integer> availableIndexes;
        private final InputStream consoleInputStream;
        private final OutputStream consoleOutputStream;
        private final Random random;

        private int questionsCount;
        @Getter
        private AbstractQuestion currentQuestion;
        @Getter
        private int maxQuestions;

        Process(List<AbstractQuestion> questions, InputStream consoleInputStream, OutputStream consoleOutputStream, Integer maxQuestions) {
            this.questions = questions;
            this.consoleInputStream = consoleInputStream;
            this.consoleOutputStream = consoleOutputStream;
            this.availableIndexes = new LinkedList<>();
            for (int i=0; i<=questions.size(); i++) {
                this.availableIndexes.add(i);
            }
            this.random = new Random(new Date().getTime());
            this.questionsCount = 0;
            this.maxQuestions = maxQuestions!=null && maxQuestions>0 ? maxQuestions : DEFAULT_MAX_QUESTIONS;
        }

        public void start() {
            // Начать задавать вопросы
        }

        public void nextQuestion() {
            if (questionsCount >= maxQuestions) {
                stop();     // достаточно
            }
            if (availableIndexes.isEmpty()) {
                stop();     // закончились
            }
            int qindex = random.nextInt(availableIndexes.size()-1);
            currentQuestion = questions.get(qindex);
            availableIndexes.remove(qindex);
            questionsCount++;
        }

        private void stop() {
            // Перестать задавать вопросы
        }
    }
}
