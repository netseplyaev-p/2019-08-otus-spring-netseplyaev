package ru.npv.exam.jc.app.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.npv.exam.jc.app.domain.app.StringPropertiesService;
import ru.npv.exam.jc.app.domain.app.utils.JarResourceProcessor;

import java.io.Reader;
import java.util.*;

@Service
public class StringPropertiesSinglePathService implements StringPropertiesService {

    @Setter
    @Getter
    private String resourcePath;

    @Getter
    private Map<String, String> cache;

    StringPropertiesSinglePathService() {
        cache = new HashMap<>();
    }

    @Autowired
    StringPropertiesSinglePathService(@Value("/settings.properties") String resourcePath) {
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