package ru.windwail.learncards.controller;

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
import ru.windwail.learncards.model.*;
import ru.windwail.learncards.repository.CategoryRepository;
import ru.windwail.learncards.repository.QuestionRepository;
import ru.windwail.learncards.service.QuestionService;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
public class MainController {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

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

        return "redirect:/category/"+formData.getCategoryId();
    }

    @GetMapping("/categories")
    @Transactional
    String categories(Model model, @SortDefault.SortDefaults(@SortDefault("name")) Pageable pageable) {
        Sort sort = Sort.by("id");
        Page<Category> categories = categoryRepository.findByParentId(null, pageable);
        CreateSubCategoryParameters subcategory = new CreateSubCategoryParameters();
        DeleteCategoryParameters deleCategory = new DeleteCategoryParameters();
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("categories", categories);
        model.addAttribute("deleCategory", deleCategory);
        return "categories";
    }

    @GetMapping("/category/{category_id}/create-question")
    @Transactional
    public String createQuestionForm(@PathVariable("category_id") Long id,
                                     Model model) {
        Category category = questionRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id)).getCategory();
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

        return "redirect:/category/"+formData.getCategoryId();
    }

    @PostMapping("/category/create-subcategory")
    @Transactional
    public String createSubCategory(@ModelAttribute("subcategory") CreateSubCategoryParameters formData,
                                    BindingResult bindingResult) {
        Category parent;
        if(formData.getCategoryId() == 0) {
            // top level
            parent = null;
        } else {
            parent = categoryRepository.findById(formData.getCategoryId()).orElseThrow(() -> new CategoryNotFoundException(formData.getCategoryId()));
        }


        Category sub = new Category();
        sub.setName(formData.getCategoryName());
        sub.setParent(parent);
        sub = categoryRepository.save(sub);

        if(parent != null ) {
            parent.getChildren().add(sub);
        }

        return "redirect:/categories";
    }

    @PostMapping("/category/{category_id}/create-question")
    @Transactional
    public String doCreateQuestion(@PathVariable("category_id") Long id,
            @Valid @ModelAttribute("question") CreateQuestionFormData formData,
                                   BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            return "edit";
        }

        Category category = categoryRepository.findById(id).orElseThrow(() -> new CategoryNotFoundException(id));

        Question q = new Question();
        q.setName(formData.getName());
        q.setQuestion(formData.getQuestion());
        q.setAnswer(formData.getAnswer());

        q = questionRepository.save(q);

        category.addQuestion(q);

        return "redirect:/category/"+id;
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
        if(bindingResult.hasErrors()) {
            model.addAttribute("editMode", EditMode.UPDATE);
            return "edit";
        }

        service.editQuestion(id, formData);

        return "redirect:/category/"+category_id;
    }

    @GetMapping("/category/{category_id}/question/{id}/delete")
    public String doDeleteUser(@PathVariable("id") Long id,
                               @PathVariable("category_id") Long category_id) {
        service.deleteQuestion(id);
        return "redirect:/category/"+category_id;
    }

    @GetMapping("/question/{id}/show")
    public String showQuestion(@PathVariable("id") Long id, Model model) {
        Question q = service.findById(id);
        model.addAttribute("question", new EditQuestionParameters(q));
        model.addAttribute("editMode", EditMode.UPDATE);
        return "show";
    }

}
