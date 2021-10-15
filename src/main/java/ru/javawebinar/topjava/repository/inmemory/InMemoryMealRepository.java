package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);


    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return repository.computeIfAbsent(userId, integer -> new ConcurrentHashMap<>()).put(meal.getId(), meal);
        }

        // handle case: update, but not present in storage
        if (ifPresent(meal.getId(), userId)) {
            return repository.get(userId).put(meal.getId(), meal);
        } else {
            return null;
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} for {}", id, userId);
        if (ifPresent(id, userId)) {
            return repository.get(userId).remove(id) != null;
        } else {
            return false;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} for {}", id, userId);
        if (ifPresent(id, userId)) {
            return repository.get(userId).get(id);
        } else {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        if (repository.isEmpty() || repository.get(userId).isEmpty()) {
            return new ArrayList<>();
        }

        return repository.get(userId).values().stream()
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getFiltered(int userId, LocalDateTime start, LocalDateTime end) {
        log.info("getFiltered");
        return new ArrayList<>();
    }

    private boolean ifPresent(int id, int userId) {
        Map<Integer, Meal> map = repository.get(userId);
        boolean result = map != null && map.get(id) != null;
        if (!result) {
            log.info("access userId={} denied to mealId={}", userId, id);
        }
        return result;

    }
}