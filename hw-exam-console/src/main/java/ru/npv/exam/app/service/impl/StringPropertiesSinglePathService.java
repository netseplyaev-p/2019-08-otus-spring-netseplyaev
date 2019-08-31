package ru.npv.exam.app.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.service.StringPropertiesService;
import ru.npv.exam.app.service.utils.JarResourceProcessor;

import java.io.Reader;
import java.util.*;

public class StringPropertiesSinglePathService implements StringPropertiesService {
    private final Logger LOG = LoggerFactory.getLogger(StringPropertiesSinglePathService.class);

    @Setter
    @Getter
    private String resourcePath;

    @Getter
    private Map<String, String> cache;

    StringPropertiesSinglePathService() {
        cache = new HashMap<>();
    }

    StringPropertiesSinglePathService(String resourcePath) {
        this();
        this.resourcePath = resourcePath;
        init();
    }

    private void init() {
        Properties properties = new Properties();
        new JarResourceProcessor(resourcePath, reader -> {
            properties.load((Reader) reader);
            cache.clear();
            properties.entrySet().forEach(e -> cache.put((String) e.getKey(), (String) e.getValue()));
        }).process();
    }

    @Override
    public Optional<String> getStringProperty(String name) {
        return Optional.ofNullable(cache.get(name));
    }
}