package com.example.todolist;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.todolist.Model.AppDatabase;
import com.example.todolist.Model.TaskDao;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented Tests for the MainActivity
 * @see com.example.todolist.MainActivity
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    AppDatabase db;
    TaskDao taskDao;

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setUp(){
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class).allowMainThreadQueries().build();
        taskDao = db.taskDao();
    }

    /**
     * Tests that the Activity launched successfully
     * @author Bryce McNary
     */
    @Test
    public void testLaunch() {
        ViewInteraction frameLayout = onView(
                allOf(withId(android.R.id.content),
                        withParent(allOf(withId(com.google.android.material.R.id.action_bar_root),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));
    }

    /**
     * Tests the onClickListener for the Fab
     * @author Bryce McNary
     *
     * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/4">Github Issue #4</a>
     *
     * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/18">Github Issue #18</a>
     *
     * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/50">Github Issue #50</a>
     */
    @Test
    public void testFabPerformClick(){
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.btnAddItem), withContentDescription("Click to add Item"),
                        childAtPosition(
                                allOf(withId(R.id.app_bar_main),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.popup_window),
                        withParent(allOf(withId(R.id.app_bar_main),
                                withParent(withId(R.id.drawer_layout)))),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.popup_window_title),
                        withParent(allOf(withId(R.id.popup_window),
                                withParent(withId(R.id.app_bar_main)))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.cancelButton),
                        withParent(allOf(withId(R.id.popup_window),
                                withParent(withId(R.id.app_bar_main)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.add_task_button),
                        withParent(allOf(withId(R.id.popup_window),
                                withParent(withId(R.id.app_bar_main)))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));
    }

    /**
     * Tests that the add task popup works as it should
     * @author Bryce McNary
     *
     * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/18">Github Issue #18</a>
     *
     * @see <a href="https://github.com/WSU-DGscheidle/spring23_project-go-team/issues/19">Github Issue #19</a>
     */
    @Test
    public void addTaskPopupTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.btnAddItem), withContentDescription("Click to add Item"),
                        childAtPosition(
                                allOf(withId(R.id.app_bar_main),
                                        childAtPosition(
                                                withId(R.id.drawer_layout),
                                                0)),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.task_name),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                2),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Test Task"), closeSoftKeyboard());

        ViewInteraction appCompatEditText1 = onView(
                allOf(withId(R.id.due_date),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                4),
                        isDisplayed()));
        appCompatEditText1.perform(replaceText("08/31/2023"));

        ViewInteraction appCompatEditText17 = onView(
                allOf(withId(R.id.reminder_time),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                5),
                        isDisplayed()));
        appCompatEditText17.perform(replaceText("12:12"), closeSoftKeyboard());

        ViewInteraction appCompatEditText18 = onView(
                allOf(withId(R.id.Prioritylev),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                6),
                        isDisplayed()));
        appCompatEditText18.perform(replaceText("Testing"), closeSoftKeyboard());

        ViewInteraction appCompatEditText19 = onView(
                allOf(withId(R.id.Prioritylev), withText("Testing"),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                6),
                        isDisplayed()));
        appCompatEditText19.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.add_task_button),
                        childAtPosition(
                                allOf(withId(R.id.popup_window),
                                        childAtPosition(
                                                withId(R.id.app_bar_main),
                                                3)),
                                8),
                        isDisplayed()));
        materialButton.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
