package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    public List<Film> getFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film newFilm);

    public List<Film> getPopularFilms(Long count);

    public void addLike(Long filmId, Long userId);

    public void deleteLike(Long filmId, Long userId);

    public Optional<Film> getFilmById(Long id);

    public Set<Long> FilmLikesId(Long id);

    public boolean checkIfFilmExists(Long id);


}
