package ru.npv.exam.jc.app.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.npv.exam.jc.app.service.utils.QuestionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
public class YesNoQuestion extends AbstractQuestion {
    @Getter
    private Map<String, String> variantsMapping;
    private final String rightVariant;

    public YesNoQuestion(String text, Map<String, String> variantsMapping, String rightVariant) {
        super(text);
        this.variantsMapping = variantsMapping;
        this.rightVariant = rightVariant;
    }

    @Override
    public QuestionType getType() {
        return QuestionType.YES_NO;
    }

    @Override
    public List<String> getVariants() {
        List<String> vars = new LinkedList<>();
        vars.addAll(variantsMapping.keySet());
        return vars;
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
