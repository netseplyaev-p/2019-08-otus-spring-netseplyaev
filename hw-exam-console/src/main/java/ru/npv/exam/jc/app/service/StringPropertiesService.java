package ru.npv.exam.jc.app.service;

import java.util.Optional;

public interface StringPropertiesService {
    Optional<String> getStringProperty(String name);
}
