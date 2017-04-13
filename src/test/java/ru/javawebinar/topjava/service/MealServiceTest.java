package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.DbPopulator;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Autowired
    private DbPopulator dbPopulator;

    @Before
    public void setUp() throws Exception {
        dbPopulator.execute();
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MEAL_04.getId(), USER_ID);
        MATCHER.assertEquals(MEAL_04, meal);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL_04.getId(), USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_03, MEAL_02, MEAL_01, MEAL_06, MEAL_05), service.getAll(USER_ID));
    }

    @Test
    public void getBetweenDates() throws Exception {
        List<Meal> meals = service.getBetweenDates(DateTimeUtil.MIN_DATE, DATE, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_06, MEAL_05, MEAL_04), meals);
    }

    @Test
    public void getBetweenDateTimes() throws Exception {
        List<Meal> meals = service.getBetweenDateTimes(DATE_TIME, LocalDateTime.MAX, USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_03, MEAL_02), meals);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(USER_ID);
        MATCHER.assertCollectionEquals(Arrays.asList(MEAL_03, MEAL_02, MEAL_01, MEAL_06, MEAL_05, MEAL_04), all);
    }

    @Test
    public void update() throws Exception {
        Meal updateMeal = new Meal(MEAL_01);
        updateMeal.setDescription("Ужин");
        service.update(updateMeal, USER_ID);
        Meal meal = service.get(MEAL_01.getId(), USER_ID);
        MATCHER.assertEquals(updateMeal, meal);
    }

    @Test
    public void save() throws Exception {
        Meal newMeal = new Meal(null, LocalDateTime.now(), "Dinner", 800);
        Meal saved = service.save(newMeal, USER_ID);
        newMeal.setId(saved.getId());
        MATCHER.assertCollectionEquals(Arrays.asList(newMeal, MEAL_03, MEAL_02, MEAL_01, MEAL_06, MEAL_05, MEAL_04),
                service.getAll(USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        service.get(MEAL_01.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        Meal updateMeal = new Meal(MEAL_01);
        updateMeal.setDescription("Ужин");
        service.update(updateMeal, ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(MEAL_02.getId(), ADMIN_ID);
    }
}