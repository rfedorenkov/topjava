package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("meal_id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("dateTime", meal.getDateTime());

        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(map);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET description=:description, calories=:calories, dateTime=:dateTime WHERE meal_id=:userId", map) == 0) {
            return null;
        }
        return meal;
    }


    //@Override
    //    public User save(User user) {
    //        MapSqlParameterSource map = new MapSqlParameterSource()
    //                .addValue("id", user.getId())
    //                .addValue("name", user.getName())
    //                .addValue("email", user.getEmail())
    //                .addValue("password", user.getPassword())
    //                .addValue("registered", user.getRegistered())
    //                .addValue("enabled", user.isEnabled())
    //                .addValue("caloriesPerDay", user.getCaloriesPerDay());
    //
    //        if (user.isNew()) {
    //            Number newKey = insertUser.executeAndReturnKey(map);
    //            user.setId(newKey.intValue());
    //        } else if (namedParameterJdbcTemplate.update(
    //                "UPDATE users SET name=:name, email=:email, password=:password, " +
    //                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", map) == 0) {
    //            return null;
    //        }
    //        return user;
    //    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE meal_id=?", userId) != 0;
    }


    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE meal_id=?", ROW_MAPPER, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT * FROM meals where meal_id=?", ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query("SELECT * FROM meals WHERE datetime BETWEEN ? AND ?", ROW_MAPPER, startDateTime, endDateTime);
    }


}
