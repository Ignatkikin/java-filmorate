package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        filmValidator(film);
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Фильм успешно добавлен");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            filmValidator(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Фильм успешно обновлен");
            return oldFilm;
        }
        log.error("Фильм с id " + newFilm.getId() + " не найден");
        throw new ValidationException("Фильм с id " + newFilm.getId() + " не найден");
    }

    private void filmValidator(@RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Названия не может быть пустым");
            throw new ValidationException("Названия не может быть пустым");
        }
        if (film.getDescription().length() > 200 || film.getDescription() == null || film.getDescription().isBlank()) {
            log.error("Количество символов больше 200 или пустое");
            throw new ValidationException("Количество символом привышает 200 или пустое");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) ||
                film.getReleaseDate() == null) {
            log.error("Дата релиза не может быть раньше 1895.12.28 или пустой");
            throw new ValidationException("Дата релиза не может быть раньше 1895.12.28 или пустой");
        }
        if (film.getDuration() < 0 || film.getDuration() == null) {
            log.error("Продолжительность фильма не может быть отрицательной или пустой");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной или пустой");
        }
    }

    private long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
