package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private Set<Long> friends = new HashSet<>();
}
