package com.choe.cinema_finder.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.choe.cinema_finder.entity.Movie;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie1;
    private Movie testMovie2;

    @BeforeEach
    void setUp() {
        testMovie1 = new Movie();
        testMovie1.setTmdbId(12345L);
        testMovie1.setTitle("Test Movie 1");
        testMovie1.setOverview("Test overview 1");
        testMovie1.setPosterPath("/test1.jpg");
        testMovie1.setReleaseDate(LocalDate.of(2024, 1, 1));
        testMovie1.setVoteAverage(8.5);
        testMovie1.setIsFavorite(true);

        testMovie2 = new Movie();
        testMovie2.setTmdbId(67890L);
        testMovie2.setTitle("Test Movie 2");
        testMovie2.setOverview("Test overview 2");
        testMovie2.setPosterPath("/test2.jpg");
        testMovie2.setReleaseDate(LocalDate.of(2024, 2, 1));
        testMovie2.setVoteAverage(7.5);
        testMovie2.setIsFavorite(false);
    }

    @Test
    void testFindByTmdbId_Found() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        Optional<Movie> found = movieRepository.findByTmdbId(12345L);

        // Assert
        assertTrue(found.isPresent());
        assertEquals("Test Movie 1", found.get().getTitle());
        assertEquals(12345L, found.get().getTmdbId());
    }

    @Test
    void testFindByTmdbId_NotFound() {
        // Act
        Optional<Movie> found = movieRepository.findByTmdbId(99999L);

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByIsFavoriteTrue() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.persist(testMovie2);
        entityManager.flush();

        // Act
        List<Movie> favorites = movieRepository.findByIsFavoriteTrue();

        // Assert
        assertEquals(1, favorites.size());
        assertEquals("Test Movie 1", favorites.get(0).getTitle());
        assertTrue(favorites.get(0).getIsFavorite());
    }

    @Test
    void testFindByIsFavoriteTrue_NoFavorites() {
        // Arrange
        testMovie1.setIsFavorite(false);
        entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        List<Movie> favorites = movieRepository.findByIsFavoriteTrue();

        // Assert
        assertEquals(0, favorites.size());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.persist(testMovie2);
        entityManager.flush();

        // Act
        List<Movie> found = movieRepository.findByTitleContainingIgnoreCase("movie");

        // Assert
        assertEquals(2, found.size());
    }

    @Test
    void testFindByTitleContainingIgnoreCase_CaseInsensitive() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        List<Movie> found = movieRepository.findByTitleContainingIgnoreCase("TEST");

        // Assert
        assertEquals(1, found.size());
        assertEquals("Test Movie 1", found.get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase_NotFound() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        List<Movie> found = movieRepository.findByTitleContainingIgnoreCase("nonexistent");

        // Assert
        assertEquals(0, found.size());
    }

    @Test
    void testExistsByTmdbId_True() {
        // Arrange
        entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        boolean exists = movieRepository.existsByTmdbId(12345L);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistsByTmdbId_False() {
        // Act
        boolean exists = movieRepository.existsByTmdbId(99999L);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testSaveMovie() {
        // Act
        Movie saved = movieRepository.save(testMovie1);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("Test Movie 1", saved.getTitle());
        assertEquals(12345L, saved.getTmdbId());
    }

    @Test
    void testDeleteMovie() {
        // Arrange
        Movie saved = entityManager.persist(testMovie1);
        entityManager.flush();
        Long id = saved.getId();

        // Act
        movieRepository.deleteById(id);
        entityManager.flush();

        // Assert
        Optional<Movie> found = movieRepository.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateMovie() {
        // Arrange
        Movie saved = entityManager.persist(testMovie1);
        entityManager.flush();

        // Act
        saved.setTitle("Updated Title");
        saved.setIsFavorite(false);
        Movie updated = movieRepository.save(saved);
        entityManager.flush();

        // Assert
        assertEquals("Updated Title", updated.getTitle());
        assertFalse(updated.getIsFavorite());
    }
}