package ru.windwail.learncards.model.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.model.Tag;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

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
        this.tags =  q.getTags().stream().map(Tag::getName).collect(Collectors.joining(","));
        this.category = q.getCategory().getName();
    }

    Long id;

    String name;

    String question;

    String answer;

    Integer version;

    String tags;

    String category;
}
