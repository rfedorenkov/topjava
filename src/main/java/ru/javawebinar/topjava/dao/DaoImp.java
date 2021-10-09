package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DaoImp implements Dao {
    private final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger();

    public DaoImp() {
        initLocalDb();
    }

    public void initLocalDb() {
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        addMeal(new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void addMeal(Meal meal) {
        if (meal.isNew()) {
            meal.setId(atomicInteger.incrementAndGet());
            mealMap.put(meal.getId(), meal);
        }
        mealMap.replace(meal.getId(), meal);
    }

    @Override
    public void deleteMeal(int mealId) {
        mealMap.remove(mealId);
    }

    @Override
    public List<Meal> getAllMeals() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal getMealById(int mealId) {
        return mealMap.get(mealId);
    }
}
