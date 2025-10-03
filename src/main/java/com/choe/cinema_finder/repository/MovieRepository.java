package com.choe.cinema_finder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.choe.cinema_finder.entity.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    
    Optional<Movie> findByTmdbId(Long tmdbId);
    
    List<Movie> findByIsFavoriteTrue();
    
    List<Movie> findByTitleContainingIgnoreCase(String title);
    
    boolean existsByTmdbId(Long tmdbId);
}