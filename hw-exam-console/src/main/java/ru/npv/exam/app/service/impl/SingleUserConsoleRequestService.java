package ru.npv.exam.app.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.service.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static ru.npv.exam.app.service.utils.Constants.*;

public class SingleUserConsoleRequestService implements UserRequestService<InputStream, OutputStream> {
    private static final Logger LOG = LoggerFactory.getLogger(SingleUserConsoleRequestService.class);

    private final LoadQuestionsService questionsService;
    private final StringPropertiesService propertiesService;
    private final CheckAnswerService checkAnswerService;


    public SingleUserConsoleRequestService(LoadQuestionsService questionsService, StringPropertiesService propertiesService, CheckAnswerService checkAnswerService) {
        this.questionsService = questionsService;
        this.propertiesService = propertiesService;
        this.checkAnswerService = checkAnswerService;
    }

    @Override
    public ExamProcess getExamProcess(InputStream input, OutputStream output) {
        ExamProcess process = new ConsoleExamProcess(checkAnswerService, input, output, questionsService.load());
        Map<String, String> props = new HashMap<>();
        props.put(PROPERTY_MAX_QUESTIONS, propertiesService.getStringProperty(PROPERTY_MAX_QUESTIONS).orElse("5"));
        props.put(PROPERTY_CHECK_TRUE, propertiesService.getStringProperty(PROPERTY_CHECK_TRUE).orElse("Верно"));
        props.put(PROPERTY_CHECK_FALSE, propertiesService.getStringProperty(PROPERTY_CHECK_FALSE).orElse("Неверно"));
        props.put(PROPERTY_BEGIN_MESSAGE, propertiesService.getStringProperty(PROPERTY_BEGIN_MESSAGE).orElse("Приветствуем"));
        props.put(PROPERTY_RESULT_MESSAGE, propertiesService.getStringProperty(PROPERTY_RESULT_MESSAGE).orElse("Из %d вопросов на %d отвечено верно."));
        props.put(PROPERTY_ALL_CHECK_RESULTS, propertiesService.getStringProperty(PROPERTY_ALL_CHECK_RESULTS).orElse("false"));
        process.setParameters(props);
        LOG.debug("Создан процесс");
        return process;
    }
}
