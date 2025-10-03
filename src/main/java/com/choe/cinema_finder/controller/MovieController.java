package com.choe.cinema_finder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.choe.cinema_finder.dto.MovieDTO;
import com.choe.cinema_finder.service.MovieService;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {
    
    @Autowired
    private MovieService movieService;
    
    @GetMapping("/search")
    public ResponseEntity<List<MovieDTO>> searchMovies(@RequestParam String query) {
        List<MovieDTO> movies = movieService.searchMovies(query);
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/trending")
    public ResponseEntity<List<MovieDTO>> getTrendingMovies() {
        List<MovieDTO> movies = movieService.getTrendingMovies();
        return ResponseEntity.ok(movies);
    }
    
    @GetMapping("/favorites")
    public ResponseEntity<List<MovieDTO>> getFavorites() {
        List<MovieDTO> movies = movieService.getFavorites();
        return ResponseEntity.ok(movies);
    }
    
    @PostMapping("/favorites/{tmdbId}")
    public ResponseEntity<MovieDTO> addFavorite(@PathVariable Long tmdbId) {
        MovieDTO movie = movieService.addFavorite(tmdbId);
        return ResponseEntity.ok(movie);
    }
    
    @DeleteMapping("/favorites/{tmdbId}")
    public ResponseEntity<MovieDTO> removeFavorite(@PathVariable Long tmdbId) {
        MovieDTO movie = movieService.removeFavorite(tmdbId);
        return ResponseEntity.ok(movie);
    }
}