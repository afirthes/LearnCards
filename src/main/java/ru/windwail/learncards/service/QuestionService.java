package ru.windwail.learncards.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import ru.windwail.learncards.exception.QuestionNotFoundException;
import ru.windwail.learncards.model.Category;
import ru.windwail.learncards.model.Tag;
import ru.windwail.learncards.model.web.EditQuestionParameters;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.repository.CategoryRepository;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.repository.TagRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class QuestionService {

    @Autowired
    private QuestionRepository repository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TagRepository tagRepository;

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

    @Transactional
    public Question editQuestion(Long id, EditQuestionParameters params) {
        Question q = repository.findById(id)
                .orElseThrow(() -> new QuestionNotFoundException(id));

        if (params.getVersion() != q.getVersion()) {
            throw new ObjectOptimisticLockingFailureException(Question.class, q.getId());
        }

        // Поменялась категория
        if (!q.getCategory().getName().equalsIgnoreCase(params.getCategory())) {
            Category newCategory = categoryRepository.findByName(params.getCategory());
            Category oldCategory = q.getCategory();

            if(newCategory != null) {
                newCategory.getQuestions().add(q);
                oldCategory.getQuestions().remove(q);
                q.setCategory(newCategory);
            }
        }

        q.setAnswer(params.getAnswer());
        q.setName(params.getName());
        q.setQuestion(StringUtils.isEmpty(params.getQuestion())
                ? "{\"ops\":[{\"insert\":\""+params.getName() +"\\n\"}]}"
                : params.getQuestion());
        q.getTags().forEach(t -> t.getQuestions().remove(q));
        q.getTags().clear();

        for (String tname : params.getTags().split(",")) {

            Tag t = tagRepository.findByName(tname.toLowerCase().trim());

            if (t == null) {
                t = new Tag();
                t.setName(tname.toLowerCase().trim());
                t.getQuestions().add(q);
                t = tagRepository.save(t);

                q.getTags().add(t);
            } else {

                t.getQuestions().add(q);
                q.getTags().add(t);
            }

        }

        return q;
    }

}
