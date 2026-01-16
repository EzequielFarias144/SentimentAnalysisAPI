package com.hackathon.sentiment.api.domain.repository;

import com.hackathon.sentiment.api.domain.entity.SentimentPrediction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SentimentPredictionRepository extends JpaRepository<SentimentPrediction, UUID> {


    long countBySentiment(String sentiment);

    @Query("SELECT p FROM SentimentPrediction p JOIN FETCH p.comment")
    List<SentimentPrediction> findAllWithComments();

    @Query("SELECT p FROM SentimentPrediction p LEFT JOIN FETCH p.comment c " +
            "WHERE (:language IS NULL OR p.language = :language) " +
            "AND (:sentiment IS NULL OR p.sentiment = :sentiment) " +
            "ORDER BY p.createdAt DESC")
    List<SentimentPrediction> findWithFilters(@Param("language") String language,
                                              @Param("sentiment") String sentiment,
                                              Pageable pageable);
}