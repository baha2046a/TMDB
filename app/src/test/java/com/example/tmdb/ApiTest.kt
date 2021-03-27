package com.example.tmdb

import com.example.tmdb.api.TmdbApiService
import com.example.tmdb.io.NetworkTracker
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

    // For time measure, response time for first API call should be ignore
    // Expect Results Count: 7XX, 50XX, 0, 7XX, 0
    fun searchData() = arrayOf(
        arrayOf("tree", true), arrayOf("war", true), arrayOf("swwwtms", true), arrayOf("japan", true),
        arrayOf(
            "longQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQlongQ",
            true
        )
    )

    fun detailData() = arrayOf(arrayOf(791373), arrayOf(791374), arrayOf(791375))

    @Test
    @Parameters(method = "searchData")
    fun pageBenchmark(query: String, hasResult: Boolean) {

        val page = IntRange(1, 10)
        val queryParam: MutableMap<String, String> = HashMap()
        queryParam["api_key"] = Common.API_KEY
        queryParam["language"] = "en_US"
        queryParam["query"] = query

        NetworkTracker.testmode()

        runBlocking {
            page.forEach {
                queryParam["page"] = it.toString()
                val start = Instant.now()
                TmdbApiService.searchBenchmark(queryParam).apply {
                    val end = Instant.now()
                    println(
                        "${this.code()} ${this.body()?.results?.size ?: ""} Response Page $it: ${
                            Duration.between(
                                start,
                                end
                            ).toMillis()
                        }ms"
                    )
                    println(this)
                }
            }
            delay(5000)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    @Parameters(method = "detailData")
    fun testDetailApi(movieId: Int) {
        println("Movie Detail API Test")

        Dispatchers.setMain(Dispatchers.Unconfined)
        NetworkTracker.testmode()

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
        NetworkTracker.testmode()

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