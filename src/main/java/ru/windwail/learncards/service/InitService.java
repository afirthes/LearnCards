package ru.windwail.learncards.service;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.windwail.learncards.model.Category;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.model.Tag;
import ru.windwail.learncards.repository.CategoryRepository;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.repository.TagRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class InitService {

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional
    public void init() {
        Faker faker = new Faker();

        ArrayList<Tag> tags = new ArrayList<>();
        for(int i=0; i < 10; i++) {
            Tag tag = new Tag();
            tag.setName(faker.lorem().fixedString(8));
            tags.add(tagRepository.save(tag));
        }

        ArrayList<Question> set = new ArrayList<>();

        for(int i=0; i<30; i++) {
            Question q = new Question();
            q.setAnswer(faker.chuckNorris().fact());
            q.setQuestion(faker.chuckNorris().fact());
            q.setName(faker.book().title());

            Collections.shuffle(tags);
            q.addTag(tags.get(0));
            q.addTag(tags.get(1));
            q.addTag(tags.get(2));

            q = questionRepository.save(q);
            set.add(q);
        }

        Category c1 = new Category();
        c1.setName(faker.lorem().paragraph(5));
        c1.addQuestion(set.get(0));
        c1.addQuestion(set.get(1));
        c1.addQuestion(set.get(2));
        c1 = categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName(faker.lorem().paragraph(5));
        c2.setParent(c1);
        c2.addQuestion(set.get(3));
        c2.addQuestion(set.get(4));
        c2.addQuestion(set.get(5));
        c2 = categoryRepository.save(c2);

        Category c3 = new Category();
        c3.setName(faker.lorem().paragraph(5));
        c3.setParent(c2);
        c3.addQuestion(set.get(3));
        c3.addQuestion(set.get(4));
        c3.addQuestion(set.get(5));
        c3 = categoryRepository.save(c3);

        Category c4 = new Category();
        c4.setName(faker.lorem().paragraph(5));
        c4.setParent(c3);
        c4.addQuestion(set.get(3));
        c4.addQuestion(set.get(4));
        c4.addQuestion(set.get(5));
        c4 = categoryRepository.save(c4);
    }
}
