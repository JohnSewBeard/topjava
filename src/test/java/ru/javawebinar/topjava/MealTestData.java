package ru.javawebinar.topjava;

import ru.javawebinar.topjava.matcher.ModelMatcher;
import ru.javawebinar.topjava.model.BaseEntity;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Objects;

public class MealTestData {
    private static Integer mealId = BaseEntity.START_SEQ + 2;
    public static final LocalDate DATE = LocalDate.of(2015, 5, 30);
    public static final LocalDateTime DATE_TIME = LocalDateTime.of(2015, 5, 31, 13, 0);

    public static final Meal MEAL_01 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000);
    public static final Meal MEAL_02 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500);
    public static final Meal MEAL_03 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510);
    public static final Meal MEAL_04 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500);
    public static final Meal MEAL_05 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000);
    public static final Meal MEAL_06 = new Meal(mealId++, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500);

    public static final ModelMatcher<Meal> MATCHER = new ModelMatcher<>(
            ((expected, actual) -> expected == actual ||
                    Objects.equals(expected.getId(), actual.getId())
            && Objects.equals(expected.getDescription(), actual.getDescription())
            && Objects.equals(expected.getCalories(), actual.getCalories())
            && Objects.equals(expected.getDateTime(), actual.getDateTime()))
    );
}