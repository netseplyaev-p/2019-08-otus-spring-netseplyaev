package ru.npv.exam.jc.app.service.utils.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.npv.exam.jc.app.domain.app.Config;

@Component
public class ExamConfig implements Config {
    
    @Getter
    @Value("${max.questions}")
    private Integer maxQuestionsCount = 6;
    @Getter
    @Value("${begin.message}")
    private String customHelloMessage;
    @Getter
    @Value("${result.message}")
    private String customResultMessage = "Вопросов: %d. Верных ответов: %d (%d%%). Тест %s.";
    @Getter
    @Value("${check.answer.true}")
    private String trueMessage = "Верно";
    @Getter
    @Value("${check.answer.false}")
    private String falseMessage = "Неверно";
    @Getter
    @Value("${all.check.results}")
    private Boolean needResults = false;
    @Getter
    @Value("${result.passing.percent}")
    private Integer passingPercent = 85;
    @Getter
    @Value("${exit.input}")
    private String exitInput = "release me";
    @Getter
    @Value("${message.user.break}")
    private String messageUserBreak;
    @Getter
    @Value("${message.tech.error}")
    private String messageTechError;
    @Getter
    @Value("${message.process.completed}")
    private String messageProcessCompleted;
    @Getter
    @Value("${prompt.introduce}")
    private String introPrompt;
    @Getter
    @Value("${prompt.firstname}")
    private String promptName;
    @Getter
    @Value("${prompt.middlename}")
    private String promptMiddle;
    @Getter
    @Value("${prompt.lastname}")
    private String promptSurname;
    @Getter
    @Value("${error.user.exit}")
    private String errorUserExit;
    @Getter
    @Value("${prompt.exit.message}")
    private String exitPromptMessage;
    @Getter
    @Value("${message.greeting}")
    private String greetingMessage;
    @Getter
    @Value("${message.test.failed}")
    private String testFailedMessage;
    @Getter
    @Value("${message.goodbye}")
    private String goodbyeMessage;
    @Getter
    @Value("${error.undefined.question.type}")
    private String errorUndefinedQuestion;
    @Getter
    @Value("${template.question}")
    private String questionTemplate = "Вопрос %d: %s";
    @Getter
    @Value("${template.variant}")
    private String variantTemplate = "%s (%d)";
    @Getter
    @Value("${message.attempts}")
    private String attemptsMessage;
    @Getter
    @Value("${message.answer.title}")
    private String answerTitle;
    @Getter
    @Value("message.question.skipped")
    private String questionSkipped;
    @Getter
    @Value("${error.questions.ends}")
    private String errorQuestionsEnds;
    @Getter
    @Value("${result.fail}")
    private String resultFail="success";
    @Getter
    @Value("${result.success}")
    private String resultSuccess="fail";
}
