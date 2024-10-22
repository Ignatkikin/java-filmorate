package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public Film getFilmById(Long id) {
        log.info("запрос на полечения фильма с Id {}", id);
        return filmStorage.getFilmById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        film.getLikes().add(user.getId());
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        if (film.getLikes().contains(user.getId())) {
            film.getLikes().remove(user.getId());
            log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
        } else {
            log.error("Пользователь с id {} не ставил лайк фильму с id {}", userId, filmId);
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк фильму с id " + filmId);
        }
    }

    public List<Film> getPopularFilms(Long count) {
        log.info("Запрос на список популярных фильмов");
        return getFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((film1, film2) -> Integer.compare(film2.getLikes().size(), film1.getLikes().size()))
                .limit(count)
                .toList();
    }
}
