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

@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentTestFirstRun{

    @get:Rule
    var activityRule: ActivityTestRule<ActivityFirstRun> = ActivityTestRule(ActivityFirstRun::class.java)

    @Test
    fun first_run_test(){
        onView(withId(R.layout.activity_first_run))
        Thread.sleep(500)
    }

}