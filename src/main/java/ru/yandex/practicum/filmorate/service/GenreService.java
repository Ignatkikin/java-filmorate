package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Long id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }

    public Set<Genre> getFilmGenres(Long id) {
        return genreStorage.getFilmGenres(id);
    }
}
