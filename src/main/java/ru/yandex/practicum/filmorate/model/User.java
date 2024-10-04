package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class User {
    private Long id;

    @NotBlank(message = "Пустой Email")
    @Email(message = "отсутствует символ @")
    private String email;

    private String login;

    private String name;

    private LocalDate birthday;
}
