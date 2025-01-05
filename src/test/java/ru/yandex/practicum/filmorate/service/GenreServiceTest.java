package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreRepository.class, GenreRowMapper.class, GenreService.class})
public class GenreServiceTest {

    @Autowired
    GenreService genreService;

    @Test
    void testGetAllGenre() {
        List<Genre> genres = genreService.getAllGenre();
        assertEquals(6, genres.size());
    }

    @Test
    void testGetGenreById() {
        Genre genre = genreService.getGenreById(1L);
        assertEquals("Комедия", genre.getName());
    }
}
