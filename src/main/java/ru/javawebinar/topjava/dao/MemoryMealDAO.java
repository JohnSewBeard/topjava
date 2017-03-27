package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MemoryMealDAO implements MealDAO{
    private static Map<Integer, Meal> db;
    private static AtomicInteger countId = new AtomicInteger(0);

    static {
        List<Meal> meals = Arrays.asList(
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(countId.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        Map<Integer, Meal> test = meals
                .stream()
                .collect(Collectors.toMap(Meal::getId, meal -> meal));

        db = new ConcurrentHashMap<>(test);
    }

    @Override
    public List<Meal> getList() {
        return new ArrayList<>(db.values());
    }
}
