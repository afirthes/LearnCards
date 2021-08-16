package ru.windwail.learncards.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import ru.windwail.learncards.exception.QuestionNotFoundException;
import ru.windwail.learncards.model.web.EditQuestionParameters;
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

    public Question findById(Long id) {
        Question q = repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        return q;
    }

    public void deleteQuestion(Long id) {
        repository.deleteById(id);
    }

    public Question editQuestion(Long id, EditQuestionParameters params) {
        Question q = repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        if (params.getVersion() != q.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(Question.class, q.getId());
        }

        q.setQuestion(params.getQuestion());
        q.setAnswer(params.getAnswer());
        q.setName(params.getName());

        return q;
    }

}
