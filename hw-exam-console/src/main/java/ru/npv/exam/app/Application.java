package ru.npv.exam.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
    }
}
