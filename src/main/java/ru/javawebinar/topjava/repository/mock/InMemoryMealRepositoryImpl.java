package ru.javawebinar.topjava.repository.mock;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger LOG = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Table<Integer, Integer, Meal> repos = HashBasedTable.create();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(1, meal));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        String logMessage = "update";
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            logMessage = "create";
        }
        LOG.info(String.format("%s mealID: %d for userID: %d", logMessage, meal.getId(), userId));
        repos.put(userId, meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int userId, int id) {
        LOG.info(String.format("delete mealID: %d for userID: %d", id, userId));
        return repos.row(userId) != null && repos.remove(userId, id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        LOG.info(String.format("get mealID: %d for userID: %d", id, userId));
        return repos.get(userId, id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        LOG.info("getAll for userId: " + userId);
        return repos.row(userId) == null ? Collections.emptyList() : repos.row(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

