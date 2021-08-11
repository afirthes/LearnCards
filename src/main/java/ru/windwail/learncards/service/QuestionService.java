package ru.windwail.learncards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.windwail.learncards.model.CreateQuestionParameters;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.repository.QuestionRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository repository;

    public Question createQuestion(Question parameters) {
       return null;
    }

}
