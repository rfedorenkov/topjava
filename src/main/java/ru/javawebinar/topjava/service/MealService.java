package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.List;

@Service
public class MealService {

    private MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal) {
        return repository.save(meal);
    }

    public void delete(int id) {
        repository.delete(id);
        //todo checkNotFoundWithId(repository.delete(id), id);
    }

    public Meal get(int id) {
        return repository.get(id);
        //todo checkNotFoundWithId(repository.get(id), id);
    }

    public Collection<Meal> getAll() {
        return repository.getAll();
    }

    public void update(Meal meal) {
        repository.save(meal);
        //todo checkNotFoundWithId(repository.save(user), user.getId());
    }


    //// MealService можно тестировать без подмены логики авторизации,
    // поэтому в методы сервиса и репозитория
    //// мы передаем параметр userId: id авторизованного пользователя (предполагаемого владельца еды).
    //// 4.3: если еда не принадлежит авторизированному пользователю или отсутствует, в MealService бросать NotFoundException.
    //// 4.4: конвертацию в MealTo можно делать как в слое web, так и в service (Mapping Entity->DTO: Controller or Service?)
    //// 4.5: в MealService постараться сделать в каждом методе только одни запрос к MealRepository
    //// 4.6 еще раз: не надо в названиях методов повторять названия класса (Meal).
}