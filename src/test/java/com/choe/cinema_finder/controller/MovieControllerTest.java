package com.choe.cinema_finder.controller;

import com.choe.cinema_finder.dto.MovieDTO;
import com.choe.cinema_finder.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    private MovieDTO testMovieDTO;

    @BeforeEach
    void setUp() {
        testMovieDTO = new MovieDTO();
        testMovieDTO.setId(1L);
        testMovieDTO.setTmdbId(12345L);
        testMovieDTO.setTitle("Test Movie");
        testMovieDTO.setOverview("Test overview");
        testMovieDTO.setPosterPath("/test.jpg");
        testMovieDTO.setReleaseDate("2024-01-01");
        testMovieDTO.setVoteAverage(8.5);
        testMovieDTO.setIsFavorite(false);
    }

    @Test
    void testSearchMovies_Success() throws Exception {
        // Arrange
        List<MovieDTO> movies = Arrays.asList(testMovieDTO);
        when(movieService.searchMovies("test")).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/api/movies/search")
                .param("query", "test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"))
                .andExpect(jsonPath("$[0].tmdbId").value(12345))
                .andExpect(jsonPath("$[0].voteAverage").value(8.5));

        verify(movieService, times(1)).searchMovies("test");
    }

    @Test
    void testSearchMovies_EmptyResults() throws Exception {
        // Arrange
        when(movieService.searchMovies("nonexistent")).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/movies/search")
                .param("query", "nonexistent")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(movieService, times(1)).searchMovies("nonexistent");
    }

    @Test
    void testGetTrendingMovies_Success() throws Exception {
        // Arrange
        List<MovieDTO> movies = Arrays.asList(testMovieDTO);
        when(movieService.getTrendingMovies()).thenReturn(movies);

        // Act & Assert
        mockMvc.perform(get("/api/movies/trending")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"))
                .andExpect(jsonPath("$[0].tmdbId").value(12345));

        verify(movieService, times(1)).getTrendingMovies();
    }

    @Test
    void testGetFavorites_Success() throws Exception {
        // Arrange
        testMovieDTO.setIsFavorite(true);
        List<MovieDTO> favorites = Arrays.asList(testMovieDTO);
        when(movieService.getFavorites()).thenReturn(favorites);

        // Act & Assert
        mockMvc.perform(get("/api/movies/favorites")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Movie"))
                .andExpect(jsonPath("$[0].isFavorite").value(true));

        verify(movieService, times(1)).getFavorites();
    }

    @Test
    void testGetFavorites_EmptyList() throws Exception {
        // Arrange
        when(movieService.getFavorites()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/movies/favorites")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(movieService, times(1)).getFavorites();
    }

    @Test
    void testAddFavorite_Success() throws Exception {
        // Arrange
        testMovieDTO.setIsFavorite(true);
        when(movieService.addFavorite(12345L)).thenReturn(testMovieDTO);

        // Act & Assert
        mockMvc.perform(post("/api/movies/favorites/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.isFavorite").value(true));

        verify(movieService, times(1)).addFavorite(12345L);
    }

    @Test
    void testRemoveFavorite_Success() throws Exception {
        // Arrange
        testMovieDTO.setIsFavorite(false);
        when(movieService.removeFavorite(12345L)).thenReturn(testMovieDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/movies/favorites/12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Movie"))
                .andExpect(jsonPath("$.isFavorite").value(false));

        verify(movieService, times(1)).removeFavorite(12345L);
    }
}