package com.example.issac.data.settings

import org.junit.Assert.assertEquals
import org.junit.Test

class ReadingLengthTest {

    private val fullText = "Today is bright. Stay focused. Good things come."

    @Test
    fun `FULL returns the whole text`() {
        assertEquals(fullText, ReadingLength.FULL.format(fullText))
    }

    @Test
    fun `SHORT keeps only the first sentence`() {
        assertEquals("Today is bright.", ReadingLength.SHORT.format(fullText))
    }

    @Test
    fun `SHORT returns the whole text when there is a single sentence`() {
        val single = "Just one sentence here."
        assertEquals(single, ReadingLength.SHORT.format(single))
    }
}
