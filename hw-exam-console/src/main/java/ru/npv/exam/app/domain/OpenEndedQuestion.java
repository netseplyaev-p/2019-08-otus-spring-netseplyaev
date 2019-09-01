package ru.npv.exam.app.domain;

import lombok.EqualsAndHashCode;
import ru.npv.exam.app.service.utils.QuestionUtils;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class OpenEndedQuestion extends AbstractQuestion {
    private final List<String> rightVariants;

    public OpenEndedQuestion(String text, List<String> rightVariants) {
        super(text);
        this.rightVariants = rightVariants;
    }

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

    @Override
    public String toString() {
        return QuestionUtils.asString(this);
    }
}
