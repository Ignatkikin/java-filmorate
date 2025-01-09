package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenreById(Long id) {
        Optional<Genre> genreOptional = genreStorage.getGenreById(id);
        Genre genre = genreOptional.orElseThrow(() -> new NotFoundException("жанр с id " + id + " не найден"));
        return genre;
    }

    public List<Genre> getAllGenre() {
        return genreStorage.getAllGenre();
    }
}
