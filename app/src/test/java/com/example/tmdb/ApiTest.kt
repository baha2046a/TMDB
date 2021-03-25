package com.example.tmdb

import com.example.tmdb.api.TmdbApiService
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.Instant
import java.util.*

@RunWith(JUnitParamsRunner::class)
class ApiTest {

    fun searchData() = arrayOf(arrayOf("Hero", true), arrayOf("Star", true))
    fun detailData() = arrayOf(arrayOf(791373, true))

    @Test
    @Parameters(method = "detailData")
    fun testDetailApi(movieId: Int, expectResult: Int) {
        println("Movie Detail API Test")

        runBlocking {
            val start = Instant.now()
            TmdbApiService.actionGetMovieDetail(movieId) {
                val end = Instant.now()
                println("Response Time: ${Duration.between(start, end).toMillis()}ms")

                println("Results: $it")
                Assert.assertEquals(movieId, it.id)
            }

            delay(2000)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    @Parameters(method = "searchData")
    fun testSearchApi(query: String, hasResult: Boolean) {
        println("Search Movie API Test")

        val queryParam: MutableMap<String, String> = HashMap()
        queryParam["api_key"] = Common.API_KEY
        queryParam["language"] = "ja_JP"
        queryParam["page"] = "1"
        queryParam["query"] = query

        Dispatchers.setMain(Dispatchers.Unconfined)

        runBlocking {
            val start = Instant.now()
            TmdbApiService.actionSearchMovie(query, queryParam) { k, result ->
                val end = Instant.now()
                println("Response Time: ${Duration.between(start, end).toMillis()}ms")

                Assert.assertEquals(query, k)
                Assert.assertEquals(hasResult, result.isPresent)

                result.ifPresent {
                    println("Total Pages: ${it.total_pages}")
                    println("Total Results: ${it.total_results}")
                    println("Page Results: ${it.results.size}")
                    println("Results: ${it.results}")
                }
            }

            delay(5000)
        }
    }
}