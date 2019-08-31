package ru.npv.exam.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public abstract class AbstractQuestion {
    @Setter
    @Getter
    private String number;
    @Setter
    @Getter
    private String text;

    public abstract QuestionType getType();
    public abstract List<String> getVariants(); // Варианты для вывода пользователю
    public abstract List<String> getRightAnsvers(); // Варианты для сравнения
}