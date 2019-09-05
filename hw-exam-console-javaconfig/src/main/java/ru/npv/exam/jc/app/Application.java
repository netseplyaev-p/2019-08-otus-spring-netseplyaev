package ru.npv.exam.jc.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.npv.exam.jc.app.domain.app.ExamProcess;
import ru.npv.exam.jc.app.domain.app.UserRequestService;

import java.io.InputStream;
import java.io.OutputStream;

public class Application {

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        UserRequestService<InputStream, OutputStream> requestService = context.getBean(UserRequestService.class);
        ExamProcess process = requestService.getExamProcess(System.in, System.out);
        process.run();
    }
}
