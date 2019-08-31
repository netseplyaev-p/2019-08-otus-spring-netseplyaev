package ru.npv.exam.app.service.impl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.npv.exam.app.domain.AbstractQuestion;
import ru.npv.exam.app.service.LoadQuestionsService;

import java.util.List;

@NoArgsConstructor
public class LoadQuestionByCsvFileService implements LoadQuestionsService {
    @Getter
    @Setter
    private String defaultFilePath;

    public LoadQuestionByCsvFileService(String defaultFilePath) {
        this.defaultFilePath = defaultFilePath;
    }

    @Override
    public String getDefaultPath() {
        return defaultFilePath;
    }

    @Override
    public List<AbstractQuestion> load(String resourcePath) {
        return null;
    }
}
