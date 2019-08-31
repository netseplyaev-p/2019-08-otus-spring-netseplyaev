package ru.npv.exam.app.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
public abstract class AbstractQuestion {
    @Setter
    @Getter
    private String text;

    public abstract List<Answer> getAnsvers();
}