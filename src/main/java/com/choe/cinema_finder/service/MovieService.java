package com.choe.cinema_finder.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.choe.cinema_finder.dto.MovieDTO;
import com.choe.cinema_finder.dto.TMDBMovie;
import com.choe.cinema_finder.dto.TMDBResponse;
import com.choe.cinema_finder.entity.Movie;
import com.choe.cinema_finder.repository.MovieRepository;

@Service
public class MovieService {
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private TMDBService tmdbService;
    
    public List<MovieDTO> searchMovies(String query) {
        TMDBResponse response = tmdbService.searchMovies(query);
        
        if (response == null || response.getResults() == null) {
            return List.of();
        }
        
        return response.getResults().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MovieDTO> getTrendingMovies() {
        TMDBResponse response = tmdbService.getTrendingMovies();
        
        if (response == null || response.getResults() == null) {
            return List.of();
        }
        
        return response.getResults().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<MovieDTO> getFavorites() {
        return movieRepository.findByIsFavoriteTrue().stream()
                .map(MovieDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
    public MovieDTO addFavorite(Long tmdbId) {
        // Check if already exists
        Movie movie = movieRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found with tmdbId: " + tmdbId));
        
        movie.setIsFavorite(true);
        Movie saved = movieRepository.save(movie);
        return MovieDTO.fromEntity(saved);
    }
    
    public MovieDTO removeFavorite(Long tmdbId) {
        Movie movie = movieRepository.findByTmdbId(tmdbId)
                .orElseThrow(() -> new RuntimeException("Movie not found with tmdbId: " + tmdbId));
        
        movie.setIsFavorite(false);
        Movie saved = movieRepository.save(movie);
        return MovieDTO.fromEntity(saved);
    }
    
    public MovieDTO saveMovieFromTMDB(TMDBMovie tmdbMovie) {
        Movie movie = movieRepository.findByTmdbId(tmdbMovie.getId())
                .orElse(new Movie());
        
        movie.setTmdbId(tmdbMovie.getId());
        movie.setTitle(tmdbMovie.getTitle());
        movie.setOverview(tmdbMovie.getOverview());
        movie.setPosterPath(tmdbMovie.getPosterPath());
        movie.setVoteAverage(tmdbMovie.getVoteAverage());
        
        if (tmdbMovie.getReleaseDate() != null && !tmdbMovie.getReleaseDate().isEmpty()) {
            movie.setReleaseDate(LocalDate.parse(tmdbMovie.getReleaseDate()));
        }
        
        Movie saved = movieRepository.save(movie);
        return MovieDTO.fromEntity(saved);
    }

    public List<MovieDTO> getPopularMovies() {
        TMDBResponse response = tmdbService.getPopularMovies();
        
        if (response == null || response.getResults() == null) {
            return List.of();
        }
        
        return response.getResults().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private MovieDTO convertToDTO(TMDBMovie tmdbMovie) {
        // Check if movie exists in database
        boolean isFavorite = movieRepository.existsByTmdbId(tmdbMovie.getId());
        
        MovieDTO dto = new MovieDTO();
        dto.setTmdbId(tmdbMovie.getId());
        dto.setTitle(tmdbMovie.getTitle());
        dto.setOverview(tmdbMovie.getOverview());
        dto.setPosterPath(tmdbMovie.getPosterPath());
        dto.setReleaseDate(tmdbMovie.getReleaseDate());
        dto.setVoteAverage(tmdbMovie.getVoteAverage());
        dto.setIsFavorite(isFavorite);
        
        return dto;
    }
}