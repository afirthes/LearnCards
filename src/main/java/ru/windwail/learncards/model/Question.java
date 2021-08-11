package ru.windwail.learncards.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class Question {

    @Id
    @SequenceGenerator(name="question_seq",
            sequenceName="question_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="question_seq")
    Long id;

    String name;

    String question;

    String answer;

    LocalDateTime creationTime;

}
