package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import ru.npv.exam.app.service.utils.QuestionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

    @Override
    public String toString() {
        return QuestionUtils.asString(this);
    }
}
