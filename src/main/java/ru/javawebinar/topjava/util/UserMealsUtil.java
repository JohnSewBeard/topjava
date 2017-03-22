package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesForEachDay = mealList.stream()
                        .collect(Collectors.toMap(
                                m -> m.getDateTime().toLocalDate(),
                                UserMeal::getCalories,
                                (c1, c2) -> c1 + c2));

        return mealList.stream()
                .filter(userMeal ->  TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> newUserMealWithExceed(userMeal, caloriesForEachDay, caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static UserMealWithExceed newUserMealWithExceed(UserMeal userMeal, Map<LocalDate, Integer> caloriesForEachDay, int caloriesPerDay) {
        LocalDate date = userMeal.getDateTime().toLocalDate();
        return new UserMealWithExceed(
                userMeal.getDateTime(),
                userMeal.getDescription(),
                userMeal.getCalories(),
                caloriesForEachDay.get(date) > caloriesPerDay);
    }
}
