package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.AuthorizedUser.id;

@Controller
public class MealRestController {

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        service.save(id(), meal);

        response.sendRedirect("meals");
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = getId(request);
        service.delete(AuthorizedUser.id(), id);
        response.sendRedirect("meals");
    }

    public void list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = request.getParameter("fromDate");
        String endDate = request.getParameter("toDate");
        String startTime = request.getParameter("fromTime");
        String endTime = request.getParameter("toTime");

        LocalDate fromDate = StringUtils.isEmpty(startDate) ? LocalDate.MIN : LocalDate.parse(startDate);
        LocalDate toDate = StringUtils.isEmpty(endDate) ? LocalDate.MAX : LocalDate.parse(endDate);
        LocalTime fromTime = StringUtils.isEmpty(startTime) ? LocalTime.MIN : LocalTime.parse(startTime);
        LocalTime toTime = StringUtils.isEmpty(endTime) ? LocalTime.MAX: LocalTime.parse(endTime);

        HttpSession session = request.getSession();
        session.setAttribute("fromDate", StringUtils.isEmpty(startDate) ? null: fromDate);
        session.setAttribute("toDate", StringUtils.isEmpty(endDate) ? null : toDate);
        session.setAttribute("fromTime", StringUtils.isEmpty(startTime) ? null : fromTime);
        session.setAttribute("toTime", StringUtils.isEmpty(endTime) ? null : toTime);

        request.setAttribute("meals", service.getAll(id(), fromDate, toDate, fromTime, toTime));
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    public void showMealForm(HttpServletRequest request, HttpServletResponse response, String action) throws ServletException, IOException {
        Meal meal = "create".equals(action) ?
                new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                service.get(id(), getId(request));
        request.setAttribute("meal", meal);
        request.getRequestDispatcher("/meal.jsp").forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}