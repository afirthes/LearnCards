package ru.windwail.learncards.runner;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.windwail.learncards.model.Category;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.model.Tag;
import ru.windwail.learncards.repository.CategoryRepository;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.repository.TagRepository;
import ru.windwail.learncards.service.QuestionService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
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
        c1.setName("c1");
        c1.addQuestion(set.get(0));
        c1.addQuestion(set.get(1));
        c1.addQuestion(set.get(2));
        c1 = categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName("c2");
        c2.setParent(c1);
        c2.addQuestion(set.get(3));
        c2.addQuestion(set.get(4));
        c2.addQuestion(set.get(5));
        c2 = categoryRepository.save(c2);

    }

    public static void main(String[] args) {
        Faker faker = new Faker();
        System.out.println(faker.chuckNorris().fact());
        System.out.println(faker.ancient().primordial());
        System.out.println(faker.book().title());
    }
}
