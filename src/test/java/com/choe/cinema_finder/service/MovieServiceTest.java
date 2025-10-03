package com.choe.cinema_finder.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.choe.cinema_finder.dto.MovieDTO;
import com.choe.cinema_finder.dto.TMDBMovie;
import com.choe.cinema_finder.dto.TMDBResponse;
import com.choe.cinema_finder.entity.Movie;
import com.choe.cinema_finder.repository.MovieRepository;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TMDBService tmdbService;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;
    private TMDBMovie testTMDBMovie;
    private TMDBResponse testTMDBResponse;

    @BeforeEach
    void setUp() {
        // Set up test movie entity
        testMovie = new Movie();
        testMovie.setId(1L);
        testMovie.setTmdbId(12345L);
        testMovie.setTitle("Test Movie");
        testMovie.setOverview("Test overview");
        testMovie.setPosterPath("/test.jpg");
        testMovie.setReleaseDate(LocalDate.of(2024, 1, 1));
        testMovie.setVoteAverage(8.5);
        testMovie.setIsFavorite(false);

        // Set up test TMDB movie
        testTMDBMovie = new TMDBMovie();
        testTMDBMovie.setId(12345L);
        testTMDBMovie.setTitle("Test Movie");
        testTMDBMovie.setOverview("Test overview");
        testTMDBMovie.setPosterPath("/test.jpg");
        testTMDBMovie.setReleaseDate("2024-01-01");
        testTMDBMovie.setVoteAverage(8.5);

        // Set up test TMDB response
        testTMDBResponse = new TMDBResponse();
        testTMDBResponse.setResults(Arrays.asList(testTMDBMovie));
    }

    @Test
    void testSearchMovies_Success() {
        // Arrange
        when(tmdbService.searchMovies("test")).thenReturn(testTMDBResponse);
        when(movieRepository.existsByTmdbId(anyLong())).thenReturn(false);

        // Act
        List<MovieDTO> result = movieService.searchMovies("test");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
        verify(tmdbService, times(1)).searchMovies("test");
    }

    @Test
    void testSearchMovies_EmptyResults() {
        // Arrange
        TMDBResponse emptyResponse = new TMDBResponse();
        emptyResponse.setResults(Arrays.asList());
        when(tmdbService.searchMovies("nonexistent")).thenReturn(emptyResponse);

        // Act
        List<MovieDTO> result = movieService.searchMovies("nonexistent");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testSearchMovies_NullResponse() {
        // Arrange
        when(tmdbService.searchMovies("test")).thenReturn(null);

        // Act
        List<MovieDTO> result = movieService.searchMovies("test");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetTrendingMovies_Success() {
        // Arrange
        when(tmdbService.getTrendingMovies()).thenReturn(testTMDBResponse);
        when(movieRepository.existsByTmdbId(anyLong())).thenReturn(false);

        // Act
        List<MovieDTO> result = movieService.getTrendingMovies();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
        verify(tmdbService, times(1)).getTrendingMovies();
    }

    @Test
    void testGetFavorites_Success() {
        // Arrange
        testMovie.setIsFavorite(true);
        when(movieRepository.findByIsFavoriteTrue()).thenReturn(Arrays.asList(testMovie));

        // Act
        List<MovieDTO> result = movieService.getFavorites();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).getIsFavorite());
        assertEquals("Test Movie", result.get(0).getTitle());
        verify(movieRepository, times(1)).findByIsFavoriteTrue();
    }

    @Test
    void testGetFavorites_EmptyList() {
        // Arrange
        when(movieRepository.findByIsFavoriteTrue()).thenReturn(Arrays.asList());

        // Act
        List<MovieDTO> result = movieService.getFavorites();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testAddFavorite_Success() {
        // Arrange
        when(movieRepository.findByTmdbId(12345L)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        // Act
        MovieDTO result = movieService.addFavorite(12345L);

        // Assert
        assertNotNull(result);
        assertTrue(testMovie.getIsFavorite());
        verify(movieRepository, times(1)).findByTmdbId(12345L);
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    void testAddFavorite_MovieNotFound() {
        // Arrange
        when(movieRepository.findByTmdbId(99999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.addFavorite(99999L);
        });
        
        assertTrue(exception.getMessage().contains("Movie not found"));
        verify(movieRepository, times(1)).findByTmdbId(99999L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testRemoveFavorite_Success() {
        // Arrange
        testMovie.setIsFavorite(true);
        when(movieRepository.findByTmdbId(12345L)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        // Act
        MovieDTO result = movieService.removeFavorite(12345L);

        // Assert
        assertNotNull(result);
        assertFalse(testMovie.getIsFavorite());
        verify(movieRepository, times(1)).findByTmdbId(12345L);
        verify(movieRepository, times(1)).save(testMovie);
    }

    @Test
    void testRemoveFavorite_MovieNotFound() {
        // Arrange
        when(movieRepository.findByTmdbId(99999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            movieService.removeFavorite(99999L);
        });
        
        assertTrue(exception.getMessage().contains("Movie not found"));
        verify(movieRepository, times(1)).findByTmdbId(99999L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testSaveMovieFromTMDB_NewMovie() {
        // Arrange
        when(movieRepository.findByTmdbId(12345L)).thenReturn(Optional.empty());
        when(movieRepository.save(any(Movie.class))).thenAnswer(invocation -> {
            Movie savedMovie = invocation.getArgument(0);
            savedMovie.setId(1L);
            return savedMovie;
        });

        // Act
        MovieDTO result = movieService.saveMovieFromTMDB(testTMDBMovie);

        // Assert
        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
        assertEquals(12345L, result.getTmdbId());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void testSaveMovieFromTMDB_ExistingMovie() {
        // Arrange
        when(movieRepository.findByTmdbId(12345L)).thenReturn(Optional.of(testMovie));
        when(movieRepository.save(any(Movie.class))).thenReturn(testMovie);

        // Act
        MovieDTO result = movieService.saveMovieFromTMDB(testTMDBMovie);

        // Assert
        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
        verify(movieRepository, times(1)).findByTmdbId(12345L);
        verify(movieRepository, times(1)).save(testMovie);
    }
}