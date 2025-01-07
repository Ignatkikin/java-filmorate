package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static final LocalDate MIN_RELEASE_DATA = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public List<Film> getFilms() {
        log.info("запрос на полечения всех фильмов");
        List<Film> films = filmStorage.getFilms();
        for (Film film : films) {
            film.setGenres(genreStorage.getFilmGenres(film.getId()));
            Optional<Mpa> mpaOptional = mpaStorage.getMpaById(film.getMpa().getId());
            Mpa mpa = mpaOptional.orElseThrow(() -> new NotFoundException("Рейтинг с id " + film.getMpa().getId() +
                    " не найден"));
            film.setMpa(mpa);
            film.setLikes(filmStorage.filmLikesId(film.getId()));
        }
        return films;
    }

    public Film createFilm(Film film) {
        log.info("Запрос на добавления фильма");
        validateFilm(film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film newFilm) {
        log.info("Запрос на обновление фильма");
        if (newFilm.getId() == null) {
            log.error("Id должен быть указан");
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(newFilm);
        if (!filmStorage.checkIfFilmExists(newFilm.getId())) {
            throw new NotFoundException("Фильм с id " + newFilm.getId() + " не найден");
        }
        return filmStorage.updateFilm(newFilm);
    }

    public Film getFilmById(Long id) {
        log.info("запрос на полечения фильма с Id {}", id);
        Optional<Film> filmOptional = filmStorage.getFilmById(id);
        Film film = filmOptional.orElseThrow(() -> new NotFoundException("фильм с id " + id + " не найден"));
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(film.getMpa().getId());
        Mpa mpa = mpaOptional.orElseThrow(() -> new NotFoundException("Рейтинг с id " + film.getMpa().getId() +
                " не найден"));
        film.setMpa(mpa);
        film.setLikes(filmStorage.filmLikesId(id));
        return film;
    }


    public List<Film> getPopularFilms(Long count) {
        log.info("Запрос на список популярных фильмов");
        List<Film> films = filmStorage.getPopularFilms(count);
        for (Film film : films) {
            film.setGenres(genreStorage.getFilmGenres(film.getId()));
            Optional<Mpa> mpaOptional = mpaStorage.getMpaById(film.getMpa().getId());
            Mpa mpa = mpaOptional.orElseThrow(() -> new NotFoundException("Рейтинг с id " + film.getMpa().getId() +
                    " не найден"));
            film.setMpa(mpa);
            film.setLikes(filmStorage.filmLikesId(film.getId()));
        }
        return films;
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.checkIfFilmExists(filmId)) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (!userStorage.checkIfUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        Film film = getFilmById(filmId);
        if (!userStorage.checkIfUserExists(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film.getLikes().contains(userId)) {
            filmStorage.deleteLike(filmId, userId);
            log.info("Пользователь с id {} удалил лайк у фильма с id {}", userId, filmId);
        } else {
            log.error("Пользователь с id {} не ставил лайк фильму с id {}", userId, filmId);
            throw new NotFoundException("Пользователь с id " + userId + " не ставил лайк фильму с id " + filmId);
        }
    }

    public void validateFilm(Film film) {
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATA)) {
            log.error("Дата релиза не может быть раньше {} или пустой", MIN_RELEASE_DATA);
            throw new ValidationException("Дата релиза не может быть раньше " + MIN_RELEASE_DATA + " или пустой");
        }

        if (!mpaStorage.checkValid(film.getMpa().getId())) {
            log.error("Несуществующий id рейтинга {}", film.getMpa().getId());
            throw new ValidationException("Несуществующий id рейтинга " + film.getMpa().getId());
        }

        List<Genre> genres = genreStorage.getAllGenre();
        List<Long> genresId = genres.stream().map(Genre::getId).toList();
        List<Long> filmGenreId = film.getGenres().stream().map(Genre::getId).toList();
        for (Long genreId : filmGenreId) {
            if (!genresId.contains(genreId)) {
                log.error("Несуществующий id жанра {}", genreId);
                throw new ValidationException("Несуществующий id жанра " + genreId);
            }
        }
    }
}
