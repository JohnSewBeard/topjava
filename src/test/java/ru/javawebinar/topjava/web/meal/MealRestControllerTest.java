package ru.javawebinar.topjava.web.meal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MealTestData.MATCHER.contentMatcher(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                //verify that MealService doesn't contain MEAL1
                .andExpect(result -> Assert.assertTrue(!mealService.getAll(AuthorizedUser.id()).contains(MEAL1)));
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = new Meal(MEAL1);
        updated.setCalories(600);
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        MATCHER.assertEquals(updated, mealService.get(MEAL1_ID, AuthorizedUser.id()));
    }

    @Test
    public void create() throws Exception {
        Meal created = new Meal(null, LocalDateTime.now(),"Обед", 800);
        ResultActions actions = mockMvc.perform(post(REST_URL)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(JsonUtil.writeValue(created)))
                                        .andExpect(status().isOk());

        Meal returned = MATCHER.fromJsonAction(actions);
        created.setId(returned.getId());

        MATCHER.assertEquals(created, returned);
        MATCHER.assertCollectionEquals(Arrays.asList(created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1),
                mealService.getAll(AuthorizedUser.id));
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(MEALS, AuthorizedUser.getCaloriesPerDay())));
    }

    @Test
    public void getBetween() throws Exception {
        mockMvc.perform(post(REST_URL + "filter")
                .param("startDateTime", "2015-05-31T10:00:00")
                .param("endDateTime", "2015-05-31T20:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(Arrays.asList(MEAL6, MEAL5, MEAL4),
                        AuthorizedUser.getCaloriesPerDay())));
    }

    @Test
    public void getBetweenFull() throws Exception {
        mockMvc.perform(post(REST_URL + "filterFull")
                .param( "endDate", "2015-05-30")
                .param("endTime", "10:00:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MATCHER_WITH_EXCEED.contentListMatcher(MealsUtil.getWithExceeded(Collections.singletonList(MEAL1),
                        AuthorizedUser.getCaloriesPerDay())));
    }
}