package com.raquezha.heograpiya.splash

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SampleTestTest {

    private lateinit var sampleTest: SampleTest

    @BeforeEach
    fun setUp() {
        sampleTest = SampleTest()
    }

    @AfterEach
    fun tearDown() {
        print("success!")
    }

    @Test
    fun `should return true`() {
        assert(sampleTest.test())
    }
}
