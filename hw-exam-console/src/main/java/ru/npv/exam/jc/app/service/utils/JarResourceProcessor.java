package ru.npv.exam.jc.app.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class JarResourceProcessor {
    private final Logger LOG = LoggerFactory.getLogger(JarResourceProcessor.class);

    private final ThrowingConsumer<Reader, Exception> action;
    private final String resourcePath;

    public JarResourceProcessor(String resourcePath, ThrowingConsumer action) {
        this.action = action;
        this.resourcePath = resourcePath;
    }

    public void process() {
        try(InputStream resourceStream = this.getClass().getResourceAsStream(resourcePath)) {
            if (resourceStream != null) {
                Reader br = new BufferedReader(new InputStreamReader(resourceStream));
                action.accept(br);
            } else {
                LOG.error("������ "+resourcePath+" �� ������ � Jar");
            }
        } catch (IOException e) {
            LOG.error("������ ������ �������: "+resourcePath, e);
        } catch (Exception e) {
            LOG.error("����������� ������", e);
        }
    }
}
