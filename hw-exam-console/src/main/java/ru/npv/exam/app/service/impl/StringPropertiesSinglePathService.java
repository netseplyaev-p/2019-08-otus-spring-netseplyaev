package ru.npv.exam.app.service.impl;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.npv.exam.app.service.StringPropertiesService;

import java.io.*;
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
        try(InputStream resourceStream = this.getClass().getResourceAsStream(resourcePath)) {
            Properties properties = new Properties();
            if (resourceStream != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(resourceStream));
                properties.load(br);
                cache.clear();
                properties.entrySet().forEach(e -> cache.put((String) e.getKey(), (String) e.getValue()));
            } else {
                LOG.error("Ресурс не найден в Jar");
            }
        } catch (IOException e) {
            LOG.error("Ошибка чтения ресурса", e);
        }
    }

    @Override
    public String getStringProperty(String name) {
        return cache.get(name);
    }
}