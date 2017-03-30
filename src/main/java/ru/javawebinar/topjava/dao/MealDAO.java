package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {

    void add(Meal meal);

    void update(Meal meal);

    Meal get(int id);

    void delete(int id);

    List<Meal> getList();

}
