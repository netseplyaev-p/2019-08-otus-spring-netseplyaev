package ru.npv.exam.jc.app;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import ru.npv.exam.jc.app.domain.app.ExamProcess;
import ru.npv.exam.jc.app.domain.app.UserRequestService;

import java.io.InputStream;
import java.io.OutputStream;

@ComponentScan(basePackages = "ru.npv.exam.jc.app.service")
@PropertySource(value = "classpath:settings.properties")
public class Application {

    public static void main(String[] args) {
        new Application().run();
    }

    public void run() {
        ApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        UserRequestService<InputStream, OutputStream> requestService = context.getBean(UserRequestService.class);
        ExamProcess process = requestService.getExamProcess(System.in, System.out);
        process.run();
    }
}
