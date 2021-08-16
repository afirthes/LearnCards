package ru.windwail.learncards.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizAnswer implements Comparable {

    @Id
    @SequenceGenerator(name="quiz_answer_seq",
            sequenceName="quiz_answer_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="quiz_answer_seq")
    Long id;

    Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    Quiz quiz;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    Question question;

    Boolean passed;

    Integer stars;

    @UpdateTimestamp
    LocalDateTime updateTime;

    @CreationTimestamp
    LocalDateTime createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuizAnswer question = (QuizAnswer) o;

        return id != null ? id.equals(question.id) : question.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(QuizAnswer.class);
    }

    @Override
    public String toString() {
        return "QuizAnswer{" +
                "id=" + id +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        return this.getOrderId().compareTo(((QuizAnswer)o).getOrderId());
    }
}
