package ru.npv.exam.app.domain;

import lombok.EqualsAndHashCode;
import ru.npv.exam.app.service.utils.QuestionUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
public class YesNoQuestion extends AbstractQuestion {
    private Map<String, String> variantsMapping;
    private final String rightVariant;    // Да - Y, Нет - N

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
    public List<String> getRightAnsvers() {
        return Arrays.asList(rightVariant);
    }

    @Override
    public String toString() {
        return QuestionUtils.asString(this);
    }
}
