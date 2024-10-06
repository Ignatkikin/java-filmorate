package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
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
    private String login;

    private String name;

    @Past(message = "birthday не может быть в будущем")
    private LocalDate birthday;

    /*
    Анна привет, не совсем понял с проверкой валидации через аннотации.
    Если здесь над email я поставил аннотации @NotBlank и @Email, а в UserController
    в методе validateUser я убираю явную проверку этого поля на null и символа @, то почему в тестах
    я спокойно могу создать User с null полем email или отсутствующем символом @.
    Надо как то иначе тестировать? Вот не до конца смог разобраться с валидацией через аннотации, вроде выставил их
    а объект с пустым полем, все равно добавляется.
     */
}
