package ru.windwail.learncards.model.web;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.windwail.learncards.model.Question;

@Data
@NoArgsConstructor
public class AnswerParameters {

    public AnswerParameters(Question q) {
        this.name = q.getName();
        this.question = q.getQuestion();
        this.answer = q.getAnswer();
        this.version = q.getVersion();
        this.id = q.getId();
    }

    Long quizId;

    Long questionId;

    Integer orderId;

    Integer mark;

    Long id;

    String name;

    String question;

    String answer;

    Integer version;


}
