package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    public Optional<Mpa> getMpaById(Long id);

    public List<Mpa> getAllMpa();

    public boolean checkValid(Long id);
}
