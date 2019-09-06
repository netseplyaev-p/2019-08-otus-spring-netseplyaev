package ru.npv.exam.jc.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.npv.exam.jc.app.domain.app.*;
import ru.npv.exam.jc.app.service.utils.config.ExamConfig;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

@Service
public class SingleUserConsoleRequestService implements UserRequestService<InputStream, OutputStream> {
    private static final Logger LOG = LoggerFactory.getLogger(SingleUserConsoleRequestService.class);

    private final LoadQuestionsService questionsService;
    private final CheckAnswerService checkAnswerService;
    private final ExamConfig examConfig;

    public SingleUserConsoleRequestService(LoadQuestionsService questionsService, CheckAnswerService checkAnswerService, ExamConfig examConfig) {
        this.questionsService = questionsService;
        this.checkAnswerService = checkAnswerService;
        this.examConfig = examConfig;
    }

    @Override
    public ExamProcess getExamProcess(InputStream input, OutputStream output) {
        ExamProcess process = new ConsoleExamProcess(checkAnswerService, input, output, examConfig, null, new LinkedList<>(questionsService.load()));
        LOG.debug("Создан процесс {}", process);
        return process;
    }
}
