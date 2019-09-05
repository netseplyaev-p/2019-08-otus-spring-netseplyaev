package ru.npv.exam.jc.app.service.impl;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.npv.exam.jc.app.domain.model.AbstractQuestion;
import ru.npv.exam.jc.app.domain.model.QuestionType;
import ru.npv.exam.jc.app.domain.app.LoadQuestionsService;
import ru.npv.exam.jc.app.domain.app.QuestionParser;
import ru.npv.exam.jc.app.domain.app.utils.JarResourceProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Service
public class LoadQuestionByCsvFileService implements LoadQuestionsService {
    private final Logger LOG = LoggerFactory.getLogger(LoadQuestionByCsvFileService.class);

    @Getter
    private String filePath;

    private final Map<QuestionType, QuestionParser<? extends AbstractQuestion, String>> parsersByQuestionType;
    private final String separator;
    private final List<String> availableQuestionTypes;
    private List<AbstractQuestion> cache;

    public LoadQuestionByCsvFileService(List<QuestionParser<? extends AbstractQuestion, String>> parsers,
                                        @Value("${questions.path}") String filePath,
                                        @Value("${parts.separator}") String separator) {
        this.filePath = filePath;
        this.separator = separator;
        parsersByQuestionType = new HashMap<>();
        LOG.debug("Загрузка парсеров");
        parsers.forEach( p -> {
            parsersByQuestionType.put(p.getQuestionType(), p);
            LOG.debug("Загружен парсер для {}, класс вопроса {}", p.getQuestionType(), p.getQuestionClass().getName());
        });

        availableQuestionTypes = new LinkedList<>();
        for(QuestionType t: QuestionType.values()){
            availableQuestionTypes.add(String.valueOf(t));
        }
        cache = new LinkedList<>();
    }

    @Override
    public String getDefaultPath() {
        return filePath;
    }

    @Override
    public List<AbstractQuestion> load(String resourcePath) {
        if (!filePath.equals(resourcePath) || cache.isEmpty()) {
            cache = Collections.unmodifiableList(loadFromResource(resourcePath));
            filePath = resourcePath;
        }
        return cache;
    }

    private List<AbstractQuestion> loadFromResource(String resourcePath) {
        List<AbstractQuestion> questions = new LinkedList<>();
        new JarResourceProcessor(resourcePath, reader -> applyParsers((BufferedReader) reader, questions)).process();
        return questions;
    }

    private void applyParsers(BufferedReader reader, List<AbstractQuestion> questions) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (StringUtils.isEmpty(line) || line.startsWith("#")) {    // Комменты и отступы пропускаем
                continue;
            }
            String[] splitLine = line.split(separator);
            QuestionType questionType;
            if (splitLine.length > 0 && availableQuestionTypes.contains(splitLine[0].trim())
                    && (questionType = QuestionType.valueOf(splitLine[0].trim())) != null   // Только чтоб получить значение
                    && parsersByQuestionType.containsKey(questionType)) {
                LOG.debug("Попытка распарсить {}", line);
                AbstractQuestion q = parsersByQuestionType.get(questionType).parse(line);
                questions.add(q);
            } else {
                LOG.debug("Нераспознанная строка: {}", line);
            }
        }
    }
}
