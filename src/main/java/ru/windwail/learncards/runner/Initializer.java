package ru.windwail.learncards.runner;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.service.QuestionService;

import java.util.HashSet;
import java.util.Set;

//@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    QuestionService questionService;

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();

        Set<Question> set = new HashSet<>();

        for(int i=0; i<60; i++) {
            Question q = new Question();
            q.setAnswer(faker.chuckNorris().fact());
            q.setQuestion(faker.chuckNorris().fact());
            q.setName(faker.book().title());

            q = questionRepository.save(q);
            set.add(q);
        }

    }

    public static void main(String[] args) {
        Faker faker = new Faker();
        System.out.println(faker.chuckNorris().fact());
        System.out.println(faker.ancient().primordial());
        System.out.println(faker.book().title());
    }
}
