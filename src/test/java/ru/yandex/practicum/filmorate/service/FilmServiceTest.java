package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.GenreRepository;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class, FilmRowMapper.class, FilmService.class,
        UserRepository.class, UserRowMapper.class, UserService.class,
        GenreRepository.class, GenreRowMapper.class, GenreService.class,
        MpaRepository.class, MpaRowMapper.class, MpaService.class})
public class FilmServiceTest {

    @Autowired
    FilmService filmService;
    @Autowired
    UserService userService;
    @Autowired
    MpaService mpaService;
    @Autowired
    GenreService genreService;

    void createUser() {
        User user = User.builder()
                .email("IgnatKikin@yandex.ru")
                .login("IgnatKikin")
                .name("Ignat")
                .birthday(LocalDate.of(1995, 11, 22))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user);

        User user1 = User.builder()
                .email("VovaVovan@yandex.ru")
                .login("VovaVovan")
                .name("Vova")
                .birthday(LocalDate.of(2000, 7, 10))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user1);

        User user2 = User.builder()
                .email("StepanBulkin@yandex.ru")
                .login("StepanBulkin")
                .name("Stepan")
                .birthday(LocalDate.of(1990, 5, 8))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user2);

        User user3 = User.builder()
                .email("KolyaBurya@yandex.ru")
                .login("KolyaBurya")
                .name("Kolya")
                .birthday(LocalDate.of(2002, 3, 18))
                .friends(new HashSet<>())
                .build();
        userService.createUser(user3);
    }

    void createFilm() {
        Set<Genre> genresSet = new HashSet<>();
        Genre genre = genreService.getGenreById(1L);
        genresSet.add(genre);
        Film film = Film.builder()
                .name("Матрица")
                .description("Первая часть")
                .releaseDate(LocalDate.of(1999, 3, 31))
                .duration(136)
                .mpa(new Mpa(1L, "G"))
                .genres(genresSet)
                .build();
        filmService.createFilm(film);

        Set<Genre> genresSet1 = new HashSet<>();
        Genre genre1 = genreService.getGenreById(2L);
        Genre genre2 = genreService.getGenreById(6L);
        genresSet1.add(genre1);
        genresSet1.add(genre2);

        Film film1 = Film.builder()
                .name("Железный человек")
                .description("Вторая часть")
                .releaseDate(LocalDate.of(2010, 6, 15))
                .duration(125)
                .mpa(new Mpa(3L, "PG-13"))
                .genres(genresSet1)
                .build();
        filmService.createFilm(film1);

        Set<Genre> genresSet2 = new HashSet<>();
        Genre genre3 = genreService.getGenreById(3L);
        Genre genre4 = genreService.getGenreById(5L);
        genresSet2.add(genre3);
        genresSet2.add(genre4);

        Film film2 = Film.builder()
                .name("Чужая планета")
                .description("Первая часть")
                .releaseDate(LocalDate.of(2005, 3, 11))
                .duration(160)
                .mpa(new Mpa(2L, "PG"))
                .genres(genresSet2)
                .build();
        filmService.createFilm(film2);
    }

    @Test
    @DirtiesContext
    void testCreateFilm() {
        createFilm();
        List<Film> films = filmService.getFilms();
        assertEquals("Матрица", films.get(0).getName());
    }

    @Test
    @DirtiesContext
    void testUpdateFilm() {
        createFilm();

        Set<Genre> genresSet3 = new HashSet<>();
        Genre genre5 = genreService.getGenreById(3L);
        genresSet3.add(genre5);

        Film film3 = Film.builder()
                .id(1L)
                .name("Матрица")
                .description("Вторая часть")
                .releaseDate(LocalDate.of(2002, 6, 14))
                .duration(145)
                .mpa(new Mpa(2L, "PG"))
                .genres(genresSet3)
                .build();

        filmService.updateFilm(film3);

        Film film = filmService.getFilmById(1L);
        assertEquals("Вторая часть", film.getDescription());

        Set<Genre> genresSet4 = new HashSet<>();
        Genre genre6 = genreService.getGenreById(6L);
        Genre genre7 = genreService.getGenreById(5L);
        Genre genre9 = genreService.getGenreById(2L);
        genresSet4.add(genre6);
        genresSet4.add(genre7);
        genresSet4.add(genre9);

        Film film4 = Film.builder()
                .id(3L)
                .name("Наша планета")
                .description("Четвертая  часть")
                .releaseDate(LocalDate.of(2010, 2, 2))
                .duration(190)
                .mpa(new Mpa(5L, "NC-17"))
                .genres(genresSet4)
                .build();

        filmService.updateFilm(film4);
        Film film5 = filmService.getFilmById(3L);
        assertEquals("Наша планета", film5.getName());
    }

    @Test
    @DirtiesContext
    void testGetAllFilms() {
        createFilm();
        List<Film> films = filmService.getFilms();
        assertEquals(3, films.size());
    }

    @Test
    @DirtiesContext
    void testGetFilmById() {
        createFilm();
        Film film = filmService.getFilmById(2L);
        assertEquals("Железный человек", film.getName());
    }

    @Test
    @DirtiesContext
    void testAddLikeFilm() {
        createFilm();
        createUser();
        filmService.addLike(2L, 1L);
        filmService.addLike(2L, 3L);
        Film film = filmService.getFilmById(2L);
        assertEquals(2, film.getLikes().size());
    }

    @Test
    @DirtiesContext
    void testDeleteLikeFilm() {
        createFilm();
        createUser();
        filmService.addLike(2L, 1L);
        filmService.addLike(2L, 3L);
        filmService.deleteLike(2L, 1L);
        Film film = filmService.getFilmById(2L);
        assertEquals(1, film.getLikes().size());
    }

    @Test
    @DirtiesContext
    void testGetPopularFilm() {
        createFilm();
        createUser();
        filmService.addLike(3L, 2L);
        filmService.addLike(3L, 1L);
        filmService.addLike(3L, 3L);
        filmService.addLike(3L, 4L);
        filmService.addLike(2L, 1L);
        filmService.addLike(2L, 2L);
        filmService.addLike(1L, 2L);
        List<Film> popularFilms = filmService.getPopularFilms(2L);
        assertEquals("Чужая планета", popularFilms.get(0).getName());
        assertEquals("Железный человек", popularFilms.get(1).getName());
    }
}
