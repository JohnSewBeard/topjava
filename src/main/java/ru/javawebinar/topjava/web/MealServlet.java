package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MemoryMealDAO;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(MealServlet.class);
    private MealDAO mealDAO;

    public MealServlet() {
        this.mealDAO = new MemoryMealDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.debug("redirect to meals");
        resp.setContentType("text/html;charset=UTF-8");

        List<MealWithExceed> meals = MealsUtil.getFilteredWithExceeded(mealDAO.getList(),
                LocalTime.MIN, LocalTime.MAX, 2000);

        req.setAttribute("meals", meals);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
    }
}
