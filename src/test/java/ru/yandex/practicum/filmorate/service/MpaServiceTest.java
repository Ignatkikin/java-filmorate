package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.MpaRepository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({MpaRepository.class, MpaRowMapper.class, MpaService.class})
public class MpaServiceTest {

    @Autowired
    MpaService mpaService;

    @Test
    void testGetAllMpa() {
        List<Mpa> mpaList = mpaService.getAllMpa();
        assertEquals(5, mpaList.size());
    }

    @Test
    void testGetMpaById() {
        Mpa mpa = mpaService.getMpaById(1L);
        assertEquals("G", mpa.getName());
    }
}
