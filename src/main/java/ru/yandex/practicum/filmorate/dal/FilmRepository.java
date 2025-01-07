
package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Slf4j
@Repository
public class FilmRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String INSERT_FILM_QUERY = "INSERT INTO films" +
            "(name, description, releaseDate , duration, rating_mpa_id) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_mpa_id = ? WHERE film_id = ?";
    private static final String GET_FILM_BY_ID_QUERY = "SELECT * FROM films WHERE film_id = ?";
    private static final String GET_ALL_FILMS_QUERY = "SELECT * FROM films ORDER BY film_id";
    private static final String INSERT_LIKE_QUERY = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    private static final String DELETE_LIKE_QUERY = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    private static final String INSERT_GENRE_FILM_QUERY = "INSERT INTO genre_film (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_GENRE_FILM_QUERY = "DELETE FROM genre_film WHERE film_id = ?";
    private static final String GET_LIKE_FILM_QUERY = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String GET_POPULAR_FILMS_QUERY = "SELECT f.* FROM films AS f " +
            "LEFT JOIN likes l ON f.film_id = l.film_id " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(l.user_id) DESC LIMIT ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> getFilms() {
        return findMany(GET_ALL_FILMS_QUERY);
    }

    @Override
    public Film createFilm(Film film) {
        Long id = insert(INSERT_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);

        List<Object[]> batch = new ArrayList<>();
        for (Genre genre : film.getGenres()) {
            batch.add(new Object[]{film.getId(), genre.getId()});
        }
        batchUpdate(INSERT_GENRE_FILM_QUERY, batch);
        log.info("Фильм {} c id {} успешно добавлен", film.getName(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        update(UPDATE_FILM_QUERY,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId());

        if (newFilm.getGenres() != null) {
            update(DELETE_GENRE_FILM_QUERY, newFilm.getId());
            List<Object[]> batch = new ArrayList<>();
            for (Genre genre : newFilm.getGenres()) {
                batch.add(new Object[]{newFilm.getId(), genre.getId()});
            }
            batchUpdate(INSERT_GENRE_FILM_QUERY, batch);
        }
        log.info("Фильм {} c id {} успешно обновлен", newFilm.getName(), newFilm.getId());

        return newFilm;
    }

    @Override
    public Set<Long> FilmLikesId(Long id) {
        return new HashSet<>(findManyId(GET_LIKE_FILM_QUERY, id));
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return findOne(GET_FILM_BY_ID_QUERY, id);
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return findMany(GET_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public boolean checkIfFilmExists(Long id) {
        Optional<Film> filmOptional = findOne(GET_FILM_BY_ID_QUERY, id);
        return filmOptional.isPresent();
    }

}


