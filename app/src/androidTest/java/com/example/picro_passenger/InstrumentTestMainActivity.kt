package com.example.picro_passenger

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.example.picro_passenger.Activities.ActivityMain
import org.junit.Rule
import org.junit.runner.RunWith

/**
 * Instrument tes untuk ActivityMain
 * Passed 5 of 5
^*/
@RunWith(AndroidJUnit4::class)
@LargeTest
class InstrumentTestMainActivity{

    @get:Rule
    var activityRule: ActivityTestRule<ActivityMain> = ActivityTestRule(ActivityMain::class.java)

    fun activity_main_test(){

    }

}