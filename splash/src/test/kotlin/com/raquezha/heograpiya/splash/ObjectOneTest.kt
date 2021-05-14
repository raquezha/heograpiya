package com.raquezha.heograpiya.splash

import io.mockk.clearMocks
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ObjectOneTest {

    private var sampleTest: SampleTest = mockk(relaxed = true)
    private lateinit var objectOne: ObjectOne

    @BeforeEach
    fun setUp() {
        clearMocks(sampleTest)
        objectOne = ObjectOne(sampleTest)
    }

    @Test
    fun `should return 1`() {
        assertEquals(objectOne.getObject1(), "1")
    }
}
