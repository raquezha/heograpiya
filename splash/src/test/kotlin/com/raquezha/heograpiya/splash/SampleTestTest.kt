package com.raquezha.heograpiya.splash


import io.mockk.clearMocks
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class SampleTestTest {

    private val sampleTest: SampleTest = mockk(relaxed = true)

    @BeforeEach
    fun init() {
        clearMocks(sampleTest)
    }

    @Test
    fun `should return true`() {
        val sampleTest = SampleTest().test()
        assert(sampleTest)
    }
}