package ru.npv.exam.app.service;

import java.util.Optional;

public interface StringPropertiesService {
    Optional<String> getStringProperty(String name);
}
