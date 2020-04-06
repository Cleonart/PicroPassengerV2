package com.example.picro_passenger

import com.example.picro_passenger.preuse_activities.ActivitySignIn
import com.example.picro_passenger.stored_data_controller.StoredDataHandler
import org.junit.Test

import org.junit.Assert.*

class StoredDataControllerTest {

    val sysTest = ActivitySignIn()

    @Test
    fun `should authenticate user credential for login`(){
        assertTrue(sysTest.validatingInput("Coba 123", "100000"))
    }

    @Test
    fun `should give a warning if username or password is empty`() {
        assertFalse(sysTest.validatingInput("", ""))
    }

    @Test
    fun `should give a warning password is not 6 digit`() {
        assertFalse(sysTest.validatingInput("Coba", "12345"))
    }

}