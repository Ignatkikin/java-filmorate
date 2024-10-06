package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
    private static final LocalDate MIN_RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION_SIZE = 200;
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрос на получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        film.setId(getNextFilmId());
        films.put(film.getId(), film);
        log.info("Фильм {} c id {} успешно добавлен", film.getName(), film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            validateFilm(newFilm);
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setDuration(newFilm.getDuration());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Фильм {} c id {} успешно обновлен", oldFilm.getName(), oldFilm.getId());
            return oldFilm;
        }
        log.error("Фильм с id {} не найден", newFilm.getId());
        throw new ValidationException("Фильм с id " + newFilm.getId() + " не найден");
    }

    private void validateFilm(@Valid @RequestBody Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Названия не может быть пустым");
            throw new ValidationException("Названия не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().isBlank() ||
                film.getDescription().length() > MAX_DESCRIPTION_SIZE) {
            log.error("Количество символов больше {} или пустое", MAX_DESCRIPTION_SIZE);
            throw new ValidationException("Количество символом привышает " + MAX_DESCRIPTION_SIZE + " или пустое");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATA)) {
            log.error("Дата релиза не может быть раньше {} или пустой", MIN_RELEASE_DATA);
            throw new ValidationException("Дата релиза не может быть раньше " + MIN_RELEASE_DATA + " или пустой");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
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
