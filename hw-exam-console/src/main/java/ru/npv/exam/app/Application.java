package ru.npv.exam.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.npv.exam.app.service.impl.StringPropertiesSinglePathService;

public class Application {
    private final Logger LOG = LoggerFactory.getLogger(StringPropertiesSinglePathService.class);

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    }
}
