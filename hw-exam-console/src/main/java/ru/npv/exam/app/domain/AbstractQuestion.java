package ru.npv.exam.app.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
public abstract class AbstractQuestion {
    @Setter
    @Getter
    private String text;

    public abstract QuestionType getType();
    public abstract List<String> getVariants(); // �������� ��� ������ ������������
    public abstract List<String> getRightAnswers(); // �������� ��� ���������
}