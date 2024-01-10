package com.example.todolist;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeTrue;

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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented Tests for the CompletedFragment
 * @see com.example.todolist.Model.ui.completed.CompletedFragment
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class CompletedFragmentInstrumentedTest {

    AppDatabase db = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
            AppDatabase.class, "tasks-db").allowMainThreadQueries().build();
    TaskDao taskDao = db.taskDao();

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Tests that the TextView is visible when the query results are empty
     * @author Bryce McNary
     */
    @Test
    public void emptyQueryTextViewTest() {
        assumeTrue("Test skipped, query returned multiple results", taskDao.getComplete().isEmpty());
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_complete),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.noTaskTextView), withText("No Completed Tasks"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        recyclerView.check(doesNotExist());
    }

    /**
     * Tests that the RecyclerView is visible when the query results is not empty
     * @author Bryce McNary
     */
    @Test
    public void populatedQueryRecyclerViewTest(){
        assumeTrue("Test skipped, No tasks WHERE isComplete = 1", !taskDao.getComplete().isEmpty());
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_complete),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.noTaskTextView), withText("No Completed Tasks"),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        textView.check(doesNotExist());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        withParent(withParent(withId(R.id.nav_host_fragment_content_main))),
                        isDisplayed()));
        recyclerView.check(matches(isDisplayed()));
    }

    /**
     * Tests that the bottom dialog is visible once an item in the RecyclerView is selected
     * @author Bryce McNary
     */
    @Test
    public void clickRecyclerViewItemTest(){
        assumeTrue("Test skipped, No tasks WHERE isComplete = 1", !taskDao.getComplete().isEmpty());
        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_complete),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withParent(allOf(withId(android.R.id.content),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class)))),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction bottomSheetHeader = onView(
                allOf(withId(R.id.bottom_sheet_choose), withText("Choose"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetHeader.check(matches(isDisplayed()));

        ViewInteraction bottomSheetMarkComplete = onView(
                allOf(withId(R.id.completed_bottom_sheet_markIncomplete),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetMarkComplete.check(matches(isDisplayed()));

        ViewInteraction bottomSheetDelete = onView(
                allOf(withId(R.id.completed_bottom_sheet_delete),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        bottomSheetDelete.check(matches(isDisplayed()));
    }

    /**
     * Test for clicking the delete button
     * @author Bryce McNary
     */
    @Test
    public void clickDeleteTest(){
        assumeTrue("Test skipped, No tasks WHERE isComplete = 1", !taskDao.getComplete().isEmpty());

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("com.google.android.material.appbar.AppBarLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(withId(R.id.nav_complete),
                        childAtPosition(
                                allOf(withId(com.google.android.material.R.id.design_navigation_view),
                                        childAtPosition(
                                                withId(R.id.nav_view),
                                                0)),
                                2),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.recyclerView),
                        childAtPosition(
                                withClassName(is("android.widget.RelativeLayout")),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.completed_bottom_sheet_delete),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction frameLayout = onView(
                allOf(withId(android.R.id.content),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        frameLayout.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.cancel_delete_button), withText("Cancel"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.delete_button), withText("Delete"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.cancel_delete_button), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        3),
                                1),
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
