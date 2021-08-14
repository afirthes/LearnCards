package ru.windwail.learncards.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.validation.constraints.NotNull;
import javax.annotation.processing.Generated;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
public class Question {

    @Id
    @SequenceGenerator(name="question_seq",
            sequenceName="question_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="question_seq")
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany
    @JoinTable(name = "question_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<Tag> tags = new HashSet<>();

    String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String question;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String answer;

    LocalDateTime creationTime;

    @Version
    private Integer version;

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.getQuestions().add(this);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getQuestions().remove(this);
    }

    public void removeTags() {
        Iterator<Tag> iterator = this.tags.iterator();

        while(iterator.hasNext()) {
            Tag tag = iterator.next();

            tag.getQuestions().remove(this);
            iterator.remove();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Question.class);
    }
}
