package ru.javawebinar.topjava.service.meal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

@ActiveProfiles({Profiles.ACTIVE_DB, "datajpa"})
public class MealServiceDataJPATest extends MealServiceTest{

    @Test
    public void getWithUser() {
        Meal meal = getService().getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        Assert.assertEquals(ADMIN.getName(), meal.getUser().getName());
    }

}
