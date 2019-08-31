package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class CloseEndingQuestion extends AbstractQuestion {
    private final String rightVariant;
    private final List<String> variants;

    @Override
    public QuestionType getType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public List<String> getVariants() {
        return variants;
    }

    @Override
    public List<String> getRightAnsvers() {
        return Arrays.asList(rightVariant);
    }
}
