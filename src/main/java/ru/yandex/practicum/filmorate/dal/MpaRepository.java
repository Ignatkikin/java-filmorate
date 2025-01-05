package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepository extends BaseRepository<Mpa> implements MpaStorage {
    private static final String GET_MPA_BY_ID_QUERY = "SELECT * FROM rating_mpa WHERE rating_id = ?";
    private static final String GET_ALL_MPA_QUERY = "SELECT * FROM rating_mpa ORDER BY rating_id";


    public MpaRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa getMpaById(Long id) {
        Optional<Mpa> mpaOptional = findOne(GET_MPA_BY_ID_QUERY, id);
        Mpa mpa = mpaOptional.orElseThrow(() -> new NotFoundException("рейтинг с id " + id + " не найден"));
        return mpa;
    }

    @Override
    public List<Mpa> getAllMpa() {
        return findMany(GET_ALL_MPA_QUERY);
    }

    @Override
    public boolean checkValid(Long id) {
        Optional<Mpa> mpaOptional = findOne(GET_MPA_BY_ID_QUERY, id);
        return mpaOptional.isPresent();
    }
}
