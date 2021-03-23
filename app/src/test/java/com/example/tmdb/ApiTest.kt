package com.example.tmdb

import com.example.tmdb.api.actionGetMovieDetail
import com.example.tmdb.api.actionSearchMovie
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Duration
import java.time.Instant

@RunWith(JUnitParamsRunner::class)
class ApiTest {

    fun searchData() = arrayOf(arrayOf("Hero", 200), arrayOf("", 404))
    fun detailData() = arrayOf(arrayOf(791373, 200), arrayOf(-1, 404))

    @Test
    @Parameters(method = "detailData")
    fun testDetailApi(movieId: Int, expectResult: Int) {
        println("Movie Detail API Test")

        runBlocking {
            val start = Instant.now()

            val job = async { actionGetMovieDetail(movieId) }
            val result = job.await()

            val end = Instant.now()
            println("Response Time: ${Duration.between(start, end).toMillis()}ms")

            println("Response Code: ${result.first}")
            result.second.ifPresent {
                println("Results: $it")
            }

            Assert.assertEquals(expectResult, result.first)
        }
    }

    @Test
    @Parameters(method = "searchData")
    fun testSearchApi(query: String, expectResult: Int) {
        println("Search Movie API Test")

        runBlocking {
            val start = Instant.now()

            val job = async { actionSearchMovie(query) }
            val result = job.await()

            val end = Instant.now()
            println("Response Time: ${Duration.between(start, end).toMillis()}ms")

            println("Response Code: ${result.first}")
            result.second.ifPresent {
                println("Total Pages: ${it.total_pages}")
                println("Total Results: ${it.total_results}")
                println("Page Results: ${it.results.size}")
                println("Results: ${it.results}")
            }

            Assert.assertEquals(expectResult, result.first)
        }
    }
}