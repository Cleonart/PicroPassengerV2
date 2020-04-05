package com.example.picro_passenger

import com.example.picro_passenger.stored_data_controller.StoredDataHandler
import org.junit.Test

import org.junit.Assert.*

class StoredDataControllerTest {

    val sysTest = ActivityFirstRun()

    @Test
    fun `should authenticate user credential for login`(){
        assertTrue(sysTest.validateUserCredential())
    }

}