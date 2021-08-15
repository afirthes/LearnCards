package ru.windwail.learncards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.windwail.learncards.enums.EditMode;
import ru.windwail.learncards.exception.CategoryNotFoundException;
import ru.windwail.learncards.exception.QuestionNotFoundException;
import ru.windwail.learncards.exception.QuizNotFoundException;
import ru.windwail.learncards.model.*;
import ru.windwail.learncards.repository.CategoryRepository;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.repository.QuizAnswerRepository;
import ru.windwail.learncards.repository.QuizRepository;
import ru.windwail.learncards.service.QuestionService;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    QuestionService service;

    @GetMapping("/category/{category_id}")
    @Transactional
    String main(
            @PathVariable("category_id") Long id, Model model, @SortDefault.SortDefaults(@SortDefault("id")) Pageable pageable) {
        Page<Question> questions = questionRepository.findByCategoryId(id, pageable);
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        EditCategoryParameters editCategory = new EditCategoryParameters();
        editCategory.setCategoryId(category.getId());
        editCategory.setCategoryName(category.getName());

        model.addAttribute("questions", questions);
        model.addAttribute("category", category);
        model.addAttribute("editCategory", editCategory);
        return "questions";
    }

    @PostMapping("/category/{category_id}")
    @Transactional
    public String editCategory(@ModelAttribute("editCategory") EditCategoryParameters formData,
                               BindingResult bindingResult) {

        Category category = categoryRepository.findById(formData.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(formData.getCategoryId()));

        category.setName(formData.getCategoryName());

        return "redirect:/category/" + formData.getCategoryId();
    }

    @GetMapping("/categories")
    @Transactional
    String categories(Model model, @SortDefault.SortDefaults(@SortDefault("id")) Pageable pageable) {
        List<Category> categories = categoryRepository.findByParentId(null);
        CreateSubCategoryParameters subcategory = new CreateSubCategoryParameters();
        DeleteCategoryParameters deleCategory = new DeleteCategoryParameters();
        SelectedCategories selectedCategories = new SelectedCategories();
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("categories", categories);
        model.addAttribute("deleCategory", deleCategory);
        model.addAttribute("selectedCategories", selectedCategories);
        return "categories";
    }

    @PostMapping("/quiz-setup")
    @Transactional
    String quizSetup(@ModelAttribute("selectedCategories") SelectedCategories formData, Model model) throws Exception {

        List<Category> categories = categoryRepository.findByIdIn(formData.getSelected());
        List<Question> allQuestions = categories.stream().flatMap(c -> c.getQuestions().stream()).collect(Collectors.toList());
        QuizStartParameters quizStartParameters = new QuizStartParameters();
        quizStartParameters.setNumberOfQuestions(allQuestions.size());

        ObjectMapper mapper = new ObjectMapper();
        String jsonArr = mapper.writeValueAsString(allQuestions.stream().map(Question::getId).collect(Collectors.toList()));
        quizStartParameters.setSelectedArrayJSON(jsonArr);

        model.addAttribute("totalCategories", categories.size());
        model.addAttribute("totalQuestions", allQuestions.size());
        model.addAttribute("quizStartParameters", quizStartParameters);

        return "quiz-setup";
    }

    @PostMapping("/quiz-start")
    @Transactional
    String quizStart(@ModelAttribute("quizStartParameters") QuizStartParameters formData, Model model) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        Long[] ids = mapper.readValue(formData.getSelectedArrayJSON(), Long[].class);
        List<Question> allQuestions = questionRepository.findByIdIn(ids);

        Collections.shuffle(allQuestions);

        Quiz quiz = new Quiz();
        quiz = quizRepository.save(quiz);

        for (int i = 0; i < formData.getNumberOfQuestions(); i++) {
            QuizAnswer qa = new QuizAnswer();
            qa.setQuiz(quiz);
            qa.setPassed(false);
            qa.setStars(0);
            qa.setOrderId(i);
            qa.setQuestion(allQuestions.get(i));
            quizAnswerRepository.save(qa);
            quiz.getAnswers().add(qa);
        }

        return "redirect:quiz/" + quiz.getId();
    }

    @GetMapping("/quiz/{id}")
    public String quiz(@PathVariable("id") Long id,
                       Model model) {

        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new QuizNotFoundException(id));
        List<QuizAnswer> answers = quiz.getAnswers().stream().sorted().collect(Collectors.toList());
        model.addAttribute("questions", answers);
        model.addAttribute("quiz", quiz);

        return "quiz";
    }

    @GetMapping("/category/{category_id}/create-question")
    @Transactional
    public String createQuestionForm(@PathVariable("category_id") Long id,
                                     Model model) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));
        model.addAttribute("question", new CreateQuestionFormData());
        model.addAttribute("category", category);
        model.addAttribute("editMode", EditMode.CREATE);
        return "edit";
    }


    @PostMapping("/category/delete-category")
    @Transactional
    public String deleteCategory(@ModelAttribute("deleCategory") DeleteCategoryParameters formData,
                                 BindingResult bindingResult) {
        Category category = categoryRepository.findById(formData.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(formData.getCategoryId()));

        categoryRepository.delete(category);

        return "redirect:/categories";
    }

    @PostMapping("/category/create-subcategory")
    @Transactional
    public String createSubCategory(@ModelAttribute("subcategory") CreateSubCategoryParameters formData,
                                    BindingResult bindingResult) {
        Category parent;
        if (formData.getCategoryId() == 0) {
            // top level
            parent = null;
        } else {
            parent = categoryRepository.findById(formData.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(formData.getCategoryId()));
        }


        Category sub = new Category();
        sub.setName(formData.getCategoryName());
        sub.setParent(parent);
        sub = categoryRepository.save(sub);

        if (parent != null) {
            parent.getChildren().add(sub);
        }

        return "redirect:/categories";
    }

    @PostMapping("/category/{category_id}/create-question")
    @Transactional
    public String doCreateQuestion(@PathVariable("category_id") Long id,
                                   @Valid @ModelAttribute("question") CreateQuestionFormData formData,
                                   BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "edit";
        }

        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        Question q = new Question();
        q.setName(formData.getName());
        q.setQuestion(formData.getQuestion());
        q.setAnswer(formData.getAnswer());

        q = questionRepository.save(q);

        category.addQuestion(q);

        return "redirect:/category/" + id;
    }

    @GetMapping("/category/{category_id}/question/{id}")
    public String editQuestionForm(@PathVariable("id") Long id,
                                   @PathVariable("category_id") Long category_id,
                                   Model model) {

        Category category = categoryRepository.findById(category_id).orElseThrow(() -> new CategoryNotFoundException(category_id));

        Question q = service.findById(id);
        model.addAttribute("question", new EditQuestionParameters(q));
        model.addAttribute("editMode", EditMode.UPDATE);
        model.addAttribute("category", category);
        return "edit";
    }

    @PostMapping("/category/{category_id}/question/{id}")
    public String doEditQuestion(@PathVariable("id") Long id,
                                 @PathVariable("category_id") Long category_id,
                                 @Valid @ModelAttribute("question") EditQuestionParameters formData,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "edit";
        }

        service.editQuestion(id, formData);

        return "redirect:/category/" + category_id;
    }

    @GetMapping("/category/{category_id}/question/{id}/delete")
    public String doDeleteUser(@PathVariable("id") Long id,
                               @PathVariable("category_id") Long category_id) {
        service.deleteQuestion(id);
        return "redirect:/category/" + category_id;
    }

    @GetMapping("/quiz/{quiz_id}/question/{id}")
    public String showQuestion(@PathVariable("quiz_id") Long quizId,
                               @PathVariable("id") Long id, Model model) {
        Question q = service.findById(id);

        AnswerParameters answerParameters = new AnswerParameters(q);
        answerParameters.setQuestionId(id);
        answerParameters.setQuizId(quizId);

        model.addAttribute("question", answerParameters);
        return "show";
    }

    @PostMapping("/quiz/{quiz_id}/question/{id}")
    @Transactional
    public String registerAnser(@PathVariable("quiz_id") Long quizId,
                                @PathVariable("id") Long id,
                                @ModelAttribute("question") AnswerParameters formData,
                                Model model) {

        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException(quizId));

        QuizAnswer quizAnswer = quiz.getAnswers()
                .stream()
                .filter(qa -> qa.getQuestion().getId() == id)
                .findFirst()
                .orElseThrow(() -> new QuestionNotFoundException(id));

        quizAnswer.setStars(formData.getMark());
        quizAnswer.setPassed(true);

        Optional<QuizAnswer> nextAnswer = quiz.getAnswers()
                .stream()
                .filter(qa -> qa.getOrderId() == quizAnswer.getOrderId()+1)
                .findFirst();

        if(nextAnswer.isPresent()) {
            return "redirect:/quiz/" + quizId + "/question/" + nextAnswer.get().getQuestion().getId();
        } else {
            return "redirect:/quiz/" + quizId;
        }
    }

}
