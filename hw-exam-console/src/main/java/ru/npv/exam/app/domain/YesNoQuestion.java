package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class YesNoQuestion extends AbstractQuestion {
    private Map<String, String> variantsMapping;
    private String rightVariant;    // Да - Y, Нет - N

    @Override
    public QuestionType getType() {
        return QuestionType.YES_NO;
    }

    @Override
    public List<String> getVariants() {
        return (List<String>) variantsMapping.keySet();
    }

    @Override
    public List<String> getRightAnsvers() {
        return Arrays.asList(rightVariant);
    }
}
