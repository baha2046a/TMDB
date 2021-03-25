package com.example.tmdb

import com.example.tmdb.control.MovieSearchController
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class MovieSearchControllerTest {

    @Test
    fun testSetResult() {
        val searchResult = Fixture.genSearchResult()

        MovieSearchController.query = ""

        // Value are Reset after set query to Empty
        assertEquals("", MovieSearchController.query)
        assertEquals(0, MovieSearchController.resultSet.size)
        assertEquals(false, MovieSearchController.loading)

        // Result from different query string should ignore
        MovieSearchController.setResult("Wrong Key", Optional.of(searchResult))
        assertEquals(0, MovieSearchController.resultSet.size)

        // An Empty Result
        MovieSearchController.setResult("", Optional.empty())
        assertEquals(0, MovieSearchController.resultSet.size)

        // Normal Result
        MovieSearchController.setResult("", Optional.of(searchResult))
        assertEquals(searchResult.results.size, MovieSearchController.resultSet.size)
    }

}