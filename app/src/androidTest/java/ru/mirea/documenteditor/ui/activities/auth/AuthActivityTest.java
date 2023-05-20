package ru.mirea.documenteditor.ui.activities.auth;

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
public class AuthActivityTest {

    @Rule
    public ActivityScenarioRule<AuthActivity> activityScenarioRule = new ActivityScenarioRule<>(AuthActivity.class);

    @Test
    public void checkElements(){
        onView(withId(R.id.bt_switch_button))
                .check(matches(withText("Регистрация")));
        onView(withId(R.id.tv_login))
                .check(matches(withText("Вход")));
        onView(withId(R.id.bt_login))
                .check(matches(withText("Войти")));
    }
}