package ru.windwail.learncards.model.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateQuestionFormData {

    @NotEmpty
    String name;

    String question;

    @NotEmpty
    String answer;

    String tags;

    String category;

    LocalDateTime creationTime;
}
