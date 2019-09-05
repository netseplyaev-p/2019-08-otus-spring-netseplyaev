package ru.npv.exam.jc.app.domain;

import lombok.EqualsAndHashCode;
import ru.npv.exam.jc.app.service.utils.QuestionUtils;

import java.util.Arrays;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
public class CloseEndedQuestion extends AbstractQuestion {
    private final String rightVariant;
    private final List<String> variants;

    public CloseEndedQuestion(String text, String rightVariant, List<String> variants) {
        super(text);
        this.rightVariant = rightVariant;
        this.variants = variants;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.CLOSE_ENDED;
    }

    @Override
    public List<String> getVariants() {
        return variants;
    }

    @Override
    public List<String> getRightAnswers() {
        return Arrays.asList(rightVariant);
    }

    @Override
    public String toString() {
        return QuestionUtils.asString(this);
    }
}
