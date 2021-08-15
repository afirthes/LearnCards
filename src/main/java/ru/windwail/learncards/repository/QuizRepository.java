package ru.windwail.learncards.repository;

import org.springframework.data.repository.CrudRepository;
import ru.windwail.learncards.model.Quiz;

public interface QuizRepository extends CrudRepository<Quiz, Long> {
}
