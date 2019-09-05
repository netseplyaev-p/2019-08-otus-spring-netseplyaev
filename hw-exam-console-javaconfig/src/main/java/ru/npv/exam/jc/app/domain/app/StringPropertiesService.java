package ru.npv.exam.jc.app.domain.app;

import java.util.Optional;

public interface StringPropertiesService {
    Optional<String> getStringProperty(String name);
}
