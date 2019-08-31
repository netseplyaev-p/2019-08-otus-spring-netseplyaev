package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.List;

@AllArgsConstructor
public class OpenEndingQuestion extends AbstractQuestion {
    private final List<String> rightVariants;

    @Override
    public QuestionType getType() {
        return QuestionType.OPEN_ENDED;
    }

    @Override
    public List<String> getVariants() {
        return Collections.emptyList();
    }

    @Override
    public List<String> getRightAnsvers() {
        return rightVariants;
    }


}
