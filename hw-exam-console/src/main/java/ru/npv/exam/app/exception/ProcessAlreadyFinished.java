package ru.npv.exam.app.exception;

public class ProcessAlreadyFinished extends RuntimeException {
    public ProcessAlreadyFinished(String message) {
        super(message);
    }
}
