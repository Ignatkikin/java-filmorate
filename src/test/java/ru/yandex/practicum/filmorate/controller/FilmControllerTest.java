package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void testCreateFilm() {
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
        filmController.createFilm(film);
        List<Film> films = filmController.getFilms();
        assertEquals("Матрица", films.get(0).getName());
    }

    @Test
    void testUpdateFilm() {
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
        filmController.createFilm(film);

        Film updateFilm = Film.builder()
                .id(1L)
                .name("Матрица")
                .description("Вторая часть")
                .releaseDate(LocalDate.of(2003, 5, 15))
                .duration(138)
                .build();
        filmController.updateFilm(updateFilm);
        List<Film> films = filmController.getFilms();
        assertEquals("Вторая часть", films.get(0).getDescription());
    }

    @Test
    void testGetFilms() {
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
        filmController.createFilm(film);

        Film film1 = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
        filmController.createFilm(film1);

        List<Film> films = filmController.getFilms();

        assertEquals(2, films.size());
    }

    @Test
    void testFailCreateFilmWithDateBefore1895() {
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1894, 3, 31))
                .duration(136)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.createFilm(film));
        assertTrue(exception.getMessage().contains("Дата релиза не может быть раньше 1895-12-28 или пустой"));
    }

    @Test
    void testFailUpdateFilmWithEmptyId() {
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .build();
        filmController.createFilm(film);

        Film updateFilm = Film.builder()
                .name("Матрица")
                .description("Вторая часть")
                .releaseDate(LocalDate.of(2003, 5, 15))
                .duration(138)
                .build();
        Exception exception = assertThrows(ValidationException.class, () -> filmController.updateFilm(updateFilm));
        assertTrue(exception.getMessage().contains("Id должен быть указан"));
    }
}