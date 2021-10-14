package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public Meal get(int userId) {
        log.info("get {}", userId);
        repository.values().stream().filter(meal -> meal.getUserId().equals(userId)).findFirst().get();

        return repository.get(userId);
    }

    @Override
    public Collection<Meal> getAll() {
        log.info("getAll");
        return repository.values().stream()
                .sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .collect(Collectors.toList());
    }
}


//TODO 3: Изменить MealRepository и InMemoryMealRepository таким образом, чтобы вся еда всех пользователей
// находилась в одном общем хранилище,
// но при этом каждый конкретный аутентифицированный пользователь мог видеть и редактировать только свою еду.
// 3.1: реализовать хранение еды для каждого пользователя можно с добавлением поля userId в Meal ИЛИ без него (как нравится).
// Напомню, что репозиторий один и приложение может работать одновременно с многими пользователями.
// 3.2: если по запрошенному id еда отсутствует или чужая, возвращать null/false (см. комментарии в MealRepository)
// 3.3: список еды возвращать отсортированный в обратном порядке по датам
// 3.4: дополнительно: попробуйте сделать реализацию атомарной (те учесть коллизии при одновременном изменении еды одного пользователя)