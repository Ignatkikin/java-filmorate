package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public List<Film> getFilms();

    public Film createFilm(Film film);

    public Film updateFilm(Film newFilm);

    public Film getFilmById(Long id);
}
