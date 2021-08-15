package ru.windwail.learncards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuizNotFoundException extends RuntimeException {
    public QuizNotFoundException(Long id) {
        super(String.format("Question with id %s not found", id));
    }
}