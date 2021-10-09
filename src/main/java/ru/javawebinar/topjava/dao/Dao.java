package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Dao {

    void addMeal(Meal meal);

    void deleteMeal(int mealId);

    List<Meal> getAllMeals();

    Meal getMealById(int mealId);
}
