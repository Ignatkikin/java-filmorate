package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static final LocalDate MIN_RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        validateFilm(film);
        film.setId(getNextFilmId());
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.info("Фильм {} c id {} успешно добавлен", film.getName(), film.getId());
        return film;

    }

    @Override
    public Film updateFilm(Film newFilm) {
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
        throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
    }

    @Override
    public Film getFilmById(Long id) {
        if (films.get(id) == null) {
            throw new NotFoundException("фильм с id " + id + " не найден");
        }
        return films.get(id);
    }

    public void validateFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATA)) {
            log.error("Дата релиза не может быть раньше {} или пустой", MIN_RELEASE_DATA);
            throw new ValidationException("Дата релиза не может быть раньше " + MIN_RELEASE_DATA + " или пустой");
        }
    }

    public long getNextFilmId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
