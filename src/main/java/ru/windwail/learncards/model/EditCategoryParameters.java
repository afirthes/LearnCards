package ru.windwail.learncards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCategoryParameters {

    String categoryName;

    Long categoryId;
}
