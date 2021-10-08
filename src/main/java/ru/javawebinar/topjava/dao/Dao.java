package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Dao {

    void add(Meal meal);

    void delete(int id);

    List<Meal> getAll();

    Meal getById(int id);
}
