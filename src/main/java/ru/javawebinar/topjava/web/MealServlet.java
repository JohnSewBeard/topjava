package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MemoryMealDAO;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private static final String ADD_MEAL_FORM = "add-meal-form.jsp";
    private static final String UPDATE_MEAL_FORM = "update-meal-form.jsp";
    private static final String MEALS_LIST = "meals.jsp";
    private MealDAO mealDAO;

    public MealServlet() {
        this.mealDAO = new MemoryMealDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirect to meals GET");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String action = req.getParameter("action");

        if ("add".equals(action))
            req.getRequestDispatcher(ADD_MEAL_FORM).forward(req, resp);
        else if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            Meal meal = mealDAO.get(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher(UPDATE_MEAL_FORM).forward(req, resp);
        } else if ("delete".equals(action)) {
            mealDAO.delete(Integer.parseInt(req.getParameter("id")));
            resp.sendRedirect("meals");
        } else {
            List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(mealDAO.getList(),
                    LocalTime.MIN, LocalTime.MAX, 2000);

            req.setAttribute("meals", meals);
            req.getRequestDispatcher(MEALS_LIST).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirect to meals POST");
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");

        String description = req.getParameter("description");
        int calories = Integer.valueOf(req.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));

        switch (action) {
            case "add":
                mealDAO.add(new Meal(0, dateTime, description, calories));
                resp.sendRedirect("meals");
                break;
            case "update":
                int id = Integer.parseInt(req.getParameter("id"));
                mealDAO.update(new Meal(id, dateTime, description, calories));
                resp.sendRedirect("meals");
        }
    }
}
