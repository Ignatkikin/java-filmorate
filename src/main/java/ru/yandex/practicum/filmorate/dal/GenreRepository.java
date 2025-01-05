package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class GenreRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String GET_GENRE_BY_ID_QUERY = "SELECT * FROM genre WHERE genre_id = ?";
    private static final String GET_ALL_GENRE_QUERY = "SELECT * FROM genre ORDER BY genre_id";
    private static final String GET_GENRE_FOR_FILM = "SELECT g.* FROM genre_film AS gf " +
            "LEFT JOIN genre g ON gf.genre_id = g.genre_id " +
            "WHERE gf.film_id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getGenreById(Long id) {
        Optional<Genre> genreOptional = findOne(GET_GENRE_BY_ID_QUERY, id);
        Genre genre = genreOptional.orElseThrow(() -> new NotFoundException("жанр с id " + id + " не найден"));
        return genre;
    }

    @Override
    public List<Genre> getAllGenre() {
        return findMany(GET_ALL_GENRE_QUERY);
    }

    @Override
    public Set<Genre> getFilmGenres(Long id) {
        return new HashSet<>(findMany(GET_GENRE_FOR_FILM, id));
    }
}
