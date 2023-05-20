package ru.mirea.documenteditor.ui.activities.userInfoId;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ru.mirea.documenteditor.R;

@RunWith(AndroidJUnit4.class)
public class UserInfoIdActivityTest {

    @Rule
    public ActivityScenarioRule<UserInfoIdActivity> activityScenarioRule = new ActivityScenarioRule<>(UserInfoIdActivity.class);

    @Test
    public void checkElements(){
        onView(withId(R.id.text_welcome_change_rights))
                .check(matches(withText("Добро пожаловать!")));
        onView(withId(R.id.text_change_section))
                .check(matches(withText("Добавить/изменить роль у данного пользователя для выбранного документа:")));
        onView(withId(R.id.text_document_right))
                .check(matches(withText("Документ:")));
        onView(withId(R.id.text_role_right))
                .check(matches(withText("Роль:")));
        onView(withId(R.id.bt_change_rights))
                .check(matches(withText("Применить")));
        onView(withId(R.id.bt_back))
                .check(matches(withText("Назад")));
    }
}