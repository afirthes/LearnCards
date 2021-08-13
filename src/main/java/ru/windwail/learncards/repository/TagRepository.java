package ru.windwail.learncards.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.windwail.learncards.model.Tag;

import javax.transaction.Transactional;

public interface TagRepository extends CrudRepository<Tag, Long> {


}
