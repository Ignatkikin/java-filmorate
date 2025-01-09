package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa getMpaById(Long id) {
        Optional<Mpa> mpaOptional = mpaStorage.getMpaById(id);
        Mpa mpa = mpaOptional.orElseThrow(() -> new NotFoundException("рейтинг с id " + id + " не найден"));
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
