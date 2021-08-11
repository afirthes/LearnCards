package ru.windwail.learncards.controller;

import org.hibernate.QueryTimeoutException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.windwail.learncards.enums.EditMode;
import ru.windwail.learncards.model.CreateQuestionFormData;
import ru.windwail.learncards.model.EditQuestionParameters;
import ru.windwail.learncards.model.Question;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.service.QuestionService;

import javax.validation.Valid;
import java.util.List;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class MainController {

    @Autowired
    QuestionRepository repo;

    @Autowired
    QuestionService service;

    @GetMapping("/")
    String main(Model model, @SortDefault.SortDefaults(@SortDefault("name")) Pageable pageable) {
        Sort sort = Sort.by("name");
        Page<Question> questions = repo.findAll(pageable);
        model.addAttribute("questions", questions);
        return "main";
    }

    @GetMapping("/create")
    public String createQuestionForm(Model model) {
        model.addAttribute("question", new CreateQuestionFormData());
        model.addAttribute("editMode", EditMode.CREATE);
        return "edit";
    }

    @PostMapping("/create")
    public String doCreateQuestion(@Valid @ModelAttribute("question") CreateQuestionFormData formData,
                                   BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "edit";
        }

        Question q = new Question();
        q.setName(formData.getName());
        q.setQuestion(formData.getQuestion());
        q.setAnswer(formData.getAnswer());

        repo.save(q);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String editQuestionForm(@PathVariable("id") Long id, Model model) {

        Question q = service.findById(id);
        model.addAttribute("question", new EditQuestionParameters(q));
        model.addAttribute("editMode", EditMode.UPDATE);
        return "edit";
    }

    @PostMapping("/{id}")
    public String doEditQuestion(@PathVariable("id") Long id,
                                 @Valid @ModelAttribute("question") EditQuestionParameters formData,
                                 BindingResult bindingResult,
                                 Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "edit";
        }

        service.editQuestion(id, formData);

        return "redirect:/";
    }
}
