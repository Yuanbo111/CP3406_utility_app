package com.example.issac.data.apod

import com.example.issac.data.apod.dto.ApodResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ApodRepositoryTest {

    @Test
    fun `returns the image urls, skipping videos`() = runBlocking {
        val api = FakeApodApi(
            listOf(
                ApodResponse(title = "vid", url = "https://v", mediaType = "video"),
                ApodResponse(title = "pic", url = "https://pic.jpg", mediaType = "image"),
                ApodResponse(title = "pic2", url = "https://pic2.jpg", mediaType = "image"),
            ),
        )

        val result = ApodRepository(api).getRandomImageUrls()

        assertEquals(listOf("https://pic.jpg", "https://pic2.jpg"), result.getOrNull())
    }

    @Test
    fun `fails when the response has no image`() = runBlocking {
        val api = FakeApodApi(listOf(ApodResponse(title = "vid", url = "https://v", mediaType = "video")))

        val result = ApodRepository(api).getRandomImageUrls()

        assertTrue(result.isFailure)
    }

    @Test
    fun `wraps a network error as a failed result`() = runBlocking {
        val api = FakeApodApi(error = IOException("no network"))

        val result = ApodRepository(api).getRandomImageUrls()

        assertTrue(result.isFailure)
    }
}

/** Test double for [ApodApi] — returns canned pictures or throws, no real network. */
private class FakeApodApi(
    private val pictures: List<ApodResponse> = emptyList(),
    private val error: Throwable? = null,
) : ApodApi {
    override suspend fun getRandomPictures(count: Int, apiKey: String): List<ApodResponse> {
        error?.let { throw it }
        return pictures
    }
}
