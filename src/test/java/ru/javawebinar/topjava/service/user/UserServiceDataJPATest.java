package ru.javawebinar.topjava.service.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ActiveProfiles({Profiles.ACTIVE_DB, "datajpa"})
public class UserServiceDataJPATest extends UserServiceTest{

    @Test
    public void getWithMeals() {
        User user = getService().getWithMeals(USER_ID);
        Assert.assertEquals(user.getMeals().size(), 6);
    }
}
