package ru.npv.exam.jc.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Service;
import ru.npv.exam.jc.app.domain.app.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class SingleUserConsoleRequestService implements UserRequestService<InputStream, OutputStream> {
    private static final Logger LOG = LoggerFactory.getLogger(SingleUserConsoleRequestService.class);

    private final LoadQuestionsService questionsService;
    private final CheckAnswerService checkAnswerService;
    private final AutowireCapableBeanFactory autowireCapableBeanFactory;

    public SingleUserConsoleRequestService(LoadQuestionsService questionsService, CheckAnswerService checkAnswerService, AutowireCapableBeanFactory autowireCapableBeanFactory) {
        this.questionsService = questionsService;
        this.checkAnswerService = checkAnswerService;
        this.autowireCapableBeanFactory = autowireCapableBeanFactory;
    }

    @Override
    public ExamProcess getExamProcess(InputStream input, OutputStream output) {
        ExamProcess process = new ConsoleExamProcess(checkAnswerService, input, output, new LinkedList<>(questionsService.load()));
        autowireCapableBeanFactory.autowireBeanProperties(process, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
        LOG.debug("Создан процесс {}", process);
        return process;
    }
}
