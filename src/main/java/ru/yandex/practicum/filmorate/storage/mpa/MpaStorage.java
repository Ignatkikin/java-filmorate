package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    public Mpa getMpaById(Long id);

    public List<Mpa> getAllMpa();

    public boolean checkValid(Long id);
}
