package com.example.tmdb.control

import com.example.tmdb.Fixture
import org.junit.Assert
import org.junit.Test

class FavoriteControllerTest {
    @Test
    fun testAddRemove() {
        val m1 = Fixture.genMovie()

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

    @Test
    fun testAsMovieSearchResult() {
        val m1 = Fixture.genMovie(1000)
        val m2 = Fixture.genMovie(1500)
        val m3 = Fixture.genMovie(1555)

        FavoriteController.add(m1)
        FavoriteController.add(m2)
        FavoriteController.add(m2) // Ignore

        FavoriteController.asMovieSearchResult().apply {
            Assert.assertEquals(2, this.total_results)
            Assert.assertEquals(1, this.page)
            Assert.assertEquals(1, this.total_pages)
            Assert.assertTrue(this.results.contains(m1))
            Assert.assertTrue(this.results.contains(m2))
            Assert.assertFalse(this.results.contains(m3))
        }
    }
}