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
import ru.windwail.learncards.service.InitService;
import ru.windwail.learncards.service.QuestionService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class Initializer implements CommandLineRunner {

    @Autowired
    InitService initService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initService.init();
    }

    public static void main(String[] args) {
        Faker faker = new Faker();
        System.out.println(faker.chuckNorris().fact());
        System.out.println(faker.ancient().primordial());
        System.out.println(faker.book().title());
    }
}
