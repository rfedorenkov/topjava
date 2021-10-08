package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.DaoImp;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String ADD_AND_UPDATE = "meal.jsp";
    private static final String LIST = "meals.jsp";

    private final Dao dao = new DaoImp();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String action = request.getParameter("action");
        String path;
        action = action == null ? "" : action;
        switch (action) {
            case "add" :
                log.debug("Start Add new Meal");
                Meal meal = new Meal(null, null, 0);
                path = ADD_AND_UPDATE;
                request.setAttribute("meal", meal);
                log.debug("Finish Add new Meal");
                break;
            case "update" :
                log.debug("Start Update Meal");
                int id = Integer.parseInt(request.getParameter("id"));
                meal = dao.getById(id);
                path = ADD_AND_UPDATE;
                request.setAttribute("meal", meal);
                log.debug("Finish Update Meal");
                break;
            case "delete" :
                log.debug("Start Delete Meal");
                id = Integer.parseInt(request.getParameter("id"));
                dao.delete(id);
                response.sendRedirect("meals");
                log.debug("Finish Delete Meal");
                return;
            default:
                log.debug("Start Show Meals");
                List<MealTo> meals = MealsUtil.filteredByStreams(dao.getAll(), LocalTime.MIN, LocalTime.MAX, MealsUtil.CALORIES_PER_DAY);
                request.setAttribute("meals", meals);
                path = LIST;
                log.debug("Finish Show Meals");
        }

        log.debug("forward from meals");
        request.getRequestDispatcher(path).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Start Add or Update Meal");

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        LocalDateTime dateTime = TimeUtil.parseDateTime(request.getParameter("date"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));

        Meal meal = new Meal(id, dateTime, description, calories);
        dao.add(meal);

        response.sendRedirect("meals");
        log.debug("Finish Add or Update Meal");
    }
}
