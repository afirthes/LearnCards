package ru.windwail.learncards.model.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizStartParameters {

    String selectedArrayJSON;

    Integer numberOfQuestions;


}
