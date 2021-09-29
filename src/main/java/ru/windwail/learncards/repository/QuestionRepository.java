package ru.windwail.learncards.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.windwail.learncards.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {

    Page<Question> findByCategoryId(Long categoryId, Pageable page);

    List<Question> findByIdIn(Long[] ids);

    Page<Question> findQuestionByIdIn(List<Long> ids, Pageable page);

    // Postgres specific
    @Query(value = "select q.id " +
            "from question as q " +
            "left join question_tag as qt on q.id=qt.question_id " +
            "left join tag as t on t.id=qt.tag_id " +
            "where " +
            "q.category_id = :categoryId and " +
            "(t.name ilike %:q% or q.name ilike %:q%)", nativeQuery = true)
    List<Long> filter(@Param("categoryId") Long categoryId, @Param("q") String q);

}
