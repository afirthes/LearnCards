package ru.windwail.learncards.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.repository.QuestionRepository;

import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {

    @Autowired
    QuestionRepository repo;

    @GetMapping("/")
    String main(Model model, @SortDefault.SortDefaults(@SortDefault("name")) Pageable pageable) {

        Sort sort = Sort.by("name");

        Page<Question> questions = repo.findAll(pageable);
        model.addAttribute("questions", questions);



        return "main";
    }
}
