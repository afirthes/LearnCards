package ru.windwail.learncards.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.windwail.learncards.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Page<Category> findByParentId(Long id, Pageable pageable);
}
