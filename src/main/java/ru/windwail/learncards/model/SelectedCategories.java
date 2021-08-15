package ru.windwail.learncards.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedCategories {

    List<Long> selected = new ArrayList<>();

}
