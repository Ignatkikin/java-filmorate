package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Пустой login")
    @Pattern(regexp = "\\S+", message = "login содержит пробелы")
    private String login;

    private String name;

    @Past(message = "birthday не может быть в будущем")
    private LocalDate birthday;
}
