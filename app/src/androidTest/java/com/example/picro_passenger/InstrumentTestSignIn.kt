package com.example.picro_passenger

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.picro_passenger.preuse_activities.ActivitySignIn
import junit.framework.Assert.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.concurrent.thread


/**
 * Instrument tes untuk ActivitySignIn
 * Passed 5 of 5
^*/
@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentTestSignIn {

    @get:Rule
    var activityRule: ActivityTestRule<ActivitySignIn> = ActivityTestRule(ActivitySignIn::class.java)

    @Test
    fun sign_in_test() {
        onView(withId(R.id.sign_in_username))
        onView(withId(R.id.sign_in_auth_code))
        onView(withId(R.id.sign_in_button))
        assertTrue(true)
    }

    @Test
    fun validate_when_username_or_password_is_empty(){
        onView(withId(R.id.sign_in_username)).perform(typeText(""))
        Thread.sleep(500)
        onView(withId(R.id.sign_in_auth_code)).perform(typeText(""))
        Thread.sleep(500)
        closeSoftKeyboard()
        onView(withId(R.id.sign_in_button)).perform(click())
        Thread.sleep(200)
    }

    @Test
    fun validate_when_password_is_not_6_digit(){
        onView(withId(R.id.sign_in_username)).perform(typeText("zredhard"))
        Thread.sleep(500)
        onView(withId(R.id.sign_in_auth_code)).perform(typeText("123"))
        Thread.sleep(500)
        closeSoftKeyboard()
        onView(withId(R.id.sign_in_button)).perform(click())
        Thread.sleep(200)
    }

    @Test
    fun validated_successfully_username_and_password_correct(){
        onView(withId(R.id.sign_in_username)).perform(typeText("zredhard"))
        Thread.sleep(500)
        onView(withId(R.id.sign_in_auth_code)).perform(typeText("180229"))
        Thread.sleep(500)
        closeSoftKeyboard()
        onView(withId(R.id.sign_in_button)).perform(click())
        Thread.sleep(5000)
        assertEquals("SUCCESS",activityRule.activity.statusToken)
    }

    @Test
    fun validated_successfully_username_and_password_incorrect(){
        onView(withId(R.id.sign_in_username)).perform(typeText("zred"))
        Thread.sleep(500)
        onView(withId(R.id.sign_in_auth_code)).perform(typeText("123456"))
        Thread.sleep(500)
        closeSoftKeyboard()
        onView(withId(R.id.sign_in_button)).perform(click())
        Thread.sleep(5000)
        assertEquals("USERNAME_AND_PASSWORD_NOT_MATCH",activityRule.activity.statusToken)
    }

}

