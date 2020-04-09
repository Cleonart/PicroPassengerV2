package com.example.picro_passenger

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import com.example.picro_passenger.preuse_activities.ActivitySignIn
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import com.example.picro_passenger.R
import org.robolectric.Shadows.shadowOf


@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StoredDataControllerTest {

    private var dummy_username = "I_AM_DUMMY"
    private var dummy_password = "123456"

    private lateinit var activityController: ActivityController<Activity>
    private lateinit var activity: Activity

    private lateinit var activitySignIn : ActivitySignIn

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(Activity::class.java)
        activity = activityController.get()

        // Create the view using the activity context
        activitySignIn = ActivitySignIn()
    }

    @Test
    fun `should validate true if username and password is satisfied`() {
        assertTrue(activitySignIn.validatingInput(dummy_username, dummy_password))
    }

    @Test
    fun `should give an error if username or password is empty`(){
        assertEquals(false, activitySignIn.validatingInput("", ""))
     }

    @Test
    fun `should give an error if password is less than 6 digit`(){
        assertTrue(activitySignIn.validatingInput(dummy_username, "12345"))
    }

    @Test
    fun clickingLogin_shouldStartLoginActivity() {
        val activitySplash : ActivitySplash = Robolectric.setupActivity(ActivitySplash::class.java)
        activity.findViewById<Button>(R.id.splashSignIn).performClick()

        val expectedIntent = Intent(activity, ActivitySignIn::class.java)
        val actual : Intent = shadowOf(RuntimeEnvironment.application).nextStartedActivity
        assertEquals(expectedIntent.component, actual.component)
    }
}