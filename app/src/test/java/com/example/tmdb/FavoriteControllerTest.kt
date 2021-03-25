package com.example.tmdb

import com.example.tmdb.control.FavoriteController
import org.junit.Assert
import org.junit.Test

class FavoriteControllerTest {
    @Test
    fun testAddRemove() {
        val m1 = Fixture.genMovie(111111)

        if (!FavoriteController.isExist(m1)) {
            val size = FavoriteController.asMovieSearchResult().results.size

            // add
            FavoriteController.add(m1)
            Assert.assertEquals(true, FavoriteController.isExist(m1))
            Assert.assertEquals(size + 1, FavoriteController.asMovieSearchResult().results.size)
            assert(FavoriteController.asMovieSearchResult().results.contains(m1))

            // same item add again, should not add dup movie
            FavoriteController.add(m1)
            Assert.assertEquals(true, FavoriteController.isExist(m1))
            Assert.assertEquals(size + 1, FavoriteController.asMovieSearchResult().results.size)

            // remove
            FavoriteController.remove(m1)
            Assert.assertEquals(false, FavoriteController.isExist(m1))
            Assert.assertEquals(size, FavoriteController.asMovieSearchResult().results.size)
            assert(!FavoriteController.asMovieSearchResult().results.contains(m1))

            // remove call when item not exist
            FavoriteController.remove(m1)
            Assert.assertEquals(false, FavoriteController.isExist(m1))
            Assert.assertEquals(size, FavoriteController.asMovieSearchResult().results.size)
        }
    }
}