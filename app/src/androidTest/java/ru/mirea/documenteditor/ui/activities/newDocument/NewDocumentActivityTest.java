package ru.mirea.documenteditor.ui.activities.newDocument;

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
public class NewDocumentActivityTest {

    @Rule
    public ActivityScenarioRule<NewDocumentActivity> activityScenarioRule = new ActivityScenarioRule<>(NewDocumentActivity.class);

    @Test
    public void checkElements(){
        onView(withId(R.id.text_welcome_new_document))
                .check(matches(withText("Создание нового документа!")));
        onView(withId(R.id.text_document_new_name))
                .check(matches(withText("Название:")));
        onView(withId(R.id.bt_create_document))
                .check(matches(withText("Создать новый документ")));
        onView(withId(R.id.bt_back))
                .check(matches(withText("Назад")));
    }
}