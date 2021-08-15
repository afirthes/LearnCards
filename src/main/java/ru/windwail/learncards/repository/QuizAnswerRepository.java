package ru.windwail.learncards.repository;

import org.springframework.data.repository.CrudRepository;
import ru.windwail.learncards.model.QuizAnswer;

public interface QuizAnswerRepository extends CrudRepository<QuizAnswer, Long> {
}
