package ru.windwail.learncards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @SequenceGenerator(name="category_seq",
            sequenceName="category_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="category_seq")
    Long id;

    String name;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "parent",
            orphanRemoval = true)
    Set<Category> children = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    Category parent;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "category",
            orphanRemoval = true)
    Set<Question> questions = new HashSet<>();

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.setCategory(this);
    }

    public void removeQuestion(Question question) {
        question.setCategory(null);
        this.questions.remove(question);
    }

    public void removeQuestions() {
        Iterator<Question> iterator = this.questions.iterator();

        while (iterator.hasNext()) {
            Question q = iterator.next();
            q.setCategory(null);
            iterator.remove();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category question = (Category) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Category.class);
    }
}
