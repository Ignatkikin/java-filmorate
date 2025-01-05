
package ru.yandex.practicum.filmorate.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
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


    private final MpaService mpaService;
    private final GenreService genreService;


    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper, MpaService mpaService,
                          GenreService genreService) {
        super(jdbc, mapper);
        this.mpaService = mpaService;
        this.genreService = genreService;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = findMany(GET_ALL_FILMS_QUERY);
        for (Film film : films) {
            film.setGenres(genreService.getFilmGenres(film.getId()));
            film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
            Set<Long> likesFilm = new HashSet<>(findManyId(GET_LIKE_FILM_QUERY, film.getId()));
            film.setLikes(likesFilm);
        }
        return films;
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
    public Film getFilmById(Long id) {
        Optional<Film> filmOptional = findOne(GET_FILM_BY_ID_QUERY, id);
        Film film = filmOptional.orElseThrow(() -> new NotFoundException("фильм с id " + id + " не найден"));
        film.setGenres(genreService.getFilmGenres(film.getId()));
        film.setMpa(mpaService.getMpaById(film.getMpa().getId()));
        Set<Long> likesFilm = new HashSet<>(findManyId(GET_LIKE_FILM_QUERY, id));
        film.setLikes(likesFilm);
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        List<Film> popularFilms = getFilms().stream()
                .filter(film -> film.getLikes() != null)
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count == null ? 10 : count)
                .toList();
        return popularFilms;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        update(INSERT_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        jdbc.update(DELETE_LIKE_QUERY, filmId, userId);
    }

}


