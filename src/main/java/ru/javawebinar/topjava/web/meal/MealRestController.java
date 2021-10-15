package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
        MealsUtil.meals.forEach(meal -> {
            service.create(meal, 1);
            service.create(new Meal(null, meal.getDateTime(), meal.getDescription() + " another", meal.getCalories()), 2);
        });
    }

    public Meal create(Meal meal) {
        log.info("meal {}", meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, id);
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

//    public List<MealTo> getFiltered() {
//        log.info("getFiltered");
//        return MealsUtil.getTos(service.getFiltered(authUserId()), authUserCaloriesPerDay());
//    }



//    public User getByMail(String email) {
//        log.info("getByEmail {}", email);
//        return service.getByEmail(email);
//    }

}

//TODO 4: Реализовать слои приложения для функциональности "еда".
// API контроллера должна удовлетворять все потребности демо приложения и ничего лишнего (см. демо).
// Поиск и изменение порядка сортировки в таблице демо приложения реализованы на UI (в браузере), в приложении делать не нужно.
// Смотрите на реализацию слоя для user и делаете по аналогии! Если там что-то непонятно, не надо исправлять или делать по своему.
// Задавайте вопросы. Если действительно нужна правка - я сделаю и напишу всем.
// 4.1: после авторизации (сделаем позднее), id авторизованного юзера можно получить из SecurityUtil.authUserId().
// Запрос попадает в контроллер, методы которого будут доступны снаружи по http,
// т.е. запрос можно будет сделать с ЛЮБЫМ id для еды (не принадлежащем авторизированному пользователю).
// Нельзя позволять модифицировать/смотреть чужую еду.
// 4.2: SecurityUtil может использоваться только на слое web (см. реализацию ProfileRestController).
// MealService можно тестировать без подмены логики авторизации, поэтому в методы сервиса и репозитория
// мы передаем параметр userId: id авторизованного пользователя (предполагаемого владельца еды).
// 4.3: если еда не принадлежит авторизированному пользователю или отсутствует, в MealService бросать NotFoundException.
// 4.4: конвертацию в MealTo можно делать как в слое web, так и в service (Mapping Entity->DTO: Controller or Service?)
// 4.5: в MealService постараться сделать в каждом методе только одни запрос к MealRepository
// 4.6 еще раз: не надо в названиях методов повторять названия класса (Meal).



//// MealService можно тестировать без подмены логики авторизации,
// поэтому в методы сервиса и репозитория
//// мы передаем параметр userId: id авторизованного пользователя (предполагаемого владельца еды).
//// 4.3: если еда не принадлежит авторизированному пользователю или отсутствует, в MealService бросать NotFoundException.
//// 4.4: конвертацию в MealTo можно делать как в слое web, так и в service (Mapping Entity->DTO: Controller or Service?)
//// 4.5: в MealService постараться сделать в каждом методе только одни запрос к MealRepository
//// 4.6 еще раз: не надо в названиях методов повторять названия класса (Meal).