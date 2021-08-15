package ru.windwail.learncards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {

    @Id
    @SequenceGenerator(name="quiz_seq",
            sequenceName="quiz_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="quiz_seq")
    Long id;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "quiz",
            orphanRemoval = true)
    Set<QuizAnswer> answers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quiz question = (Quiz) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Quiz.class);
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id=" + id +
                '}';
    }
}
