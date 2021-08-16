package ru.windwail.learncards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tag {

    @Id
    @SequenceGenerator(name="tag_seq",
            sequenceName="tag_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="tag_seq")
    Long id;

    String name;

    @ManyToMany(mappedBy = "tags")
    private Set<Question> questions = new HashSet<>();

    @UpdateTimestamp
    LocalDateTime updateTime;

    @CreationTimestamp
    LocalDateTime createTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        return id != null ? id.equals(tag.id) : tag.id == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Tag.class);
    }

}
