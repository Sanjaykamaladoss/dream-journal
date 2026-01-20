package com.dreamjournal.repository;

import com.dreamjournal.model.Dream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DreamRepository extends JpaRepository<Dream, Long> {

    // Find all dreams ordered by date descending (newest first)
    List<Dream> findAllByOrderByDateDesc();

    // Find dreams by mood
    List<Dream> findByMood(String mood);

    // Find lucid dreams
    List<Dream> findByLucidDreamTrue();

    // Get all tags for word cloud and theme analysis
    @Query("SELECT d.tags FROM Dream d WHERE d.tags IS NOT NULL AND d.tags <> ''")
    List<String> findAllTags();

    // Find dreams by sleep quality
    List<Dream> findBySleepQuality(Integer sleepQuality);
}
