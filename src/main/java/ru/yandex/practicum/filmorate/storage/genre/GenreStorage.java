package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {

    public Optional<Genre> getGenreById(Long id);

    public List<Genre> getAllGenre();

    public Set<Genre> getFilmGenres(Long id);
}
