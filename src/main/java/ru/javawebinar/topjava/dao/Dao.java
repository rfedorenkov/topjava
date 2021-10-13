package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {

    void addMeal(T item);

    void deleteMeal(int itemId);

    List<T> getAllMeals();

    T getMealById(int itemId);
}
