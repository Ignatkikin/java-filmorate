package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */

@Data
@Builder
public class Film {
    private Long id;
    @NotBlank(message = "Пустой name")
    private String name;


    @NotBlank(message = "Пустой description")
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @NotNull(message = "Пустой releaseDate")
    private LocalDate releaseDate;

    @Positive(message = "Отрицательный duration")
    private Integer duration;

    private Set<Long> likes;
}
