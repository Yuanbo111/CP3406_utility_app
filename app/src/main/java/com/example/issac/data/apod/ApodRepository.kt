package com.example.issac.data.apod

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

/**
 * Source of truth for the Today screen's background image. It asks [ApodApi] for
 * a batch of random NASA pictures and returns the URL of the first that is
 * actually an image (APOD sometimes serves videos, which we skip).
 *
 * Like [com.example.issac.data.horoscope.HoroscopeRepository], it reports
 * failures as a [Result] rather than throwing, so the UI can simply fall back to
 * the gradient background.
 */
@Singleton
class ApodRepository @Inject constructor(
    private val api: ApodApi,
) {

    suspend fun getRandomImageUrl(): Result<String> {
        return try {
            val imageUrl = api.getRandomPictures()
                .firstOrNull { it.mediaType == "image" && it.url.isNotBlank() }
                ?.url
            if (imageUrl != null) {
                Result.success(imageUrl)
            } else {
                Result.failure(NoSuchElementException("APOD returned no usable image"))
            }
        } catch (e: CancellationException) {
            throw e // Never swallow coroutine cancellation — always let it propagate.
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
