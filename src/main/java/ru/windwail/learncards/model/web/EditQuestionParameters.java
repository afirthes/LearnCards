package ru.windwail.learncards.model.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.windwail.learncards.model.Question;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditQuestionParameters {

    public EditQuestionParameters(Question q) {
        this.name = q.getName();
        this.question = q.getQuestion();
        this.answer = q.getAnswer();
        this.version = q.getVersion();
        this.id = q.getId();
    }

    Long id;

    String name;

    String question;

    String answer;

    Integer version;
}