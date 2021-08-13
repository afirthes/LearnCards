package ru.windwail.learncards.repository;


import org.springframework.data.repository.CrudRepository;
import ru.windwail.learncards.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
