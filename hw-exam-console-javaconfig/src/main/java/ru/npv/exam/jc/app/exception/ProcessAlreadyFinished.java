package ru.npv.exam.jc.app.exception;

public class ProcessAlreadyFinished extends RuntimeException {
    public ProcessAlreadyFinished(String message) {
        super(message);
    }
}
