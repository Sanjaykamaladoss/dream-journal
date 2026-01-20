package com.dreamjournal.controller;

import com.dreamjournal.model.Dream;
import com.dreamjournal.repository.DreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dreams")
@CrossOrigin(origins = "*")
public class DreamController {

    @Autowired
    private DreamRepository dreamRepository;

    // Get all dreams
    @GetMapping
    public ResponseEntity<List<Dream>> getAllDreams() {
        try {
            List<Dream> dreams = dreamRepository.findAllByOrderByDateDesc();
            return new ResponseEntity<>(dreams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get dream by ID
    @GetMapping("/{id}")
    public ResponseEntity<Dream> getDreamById(@PathVariable Long id) {
        Optional<Dream> dreamData = dreamRepository.findById(id);
        return dreamData.map(dream -> new ResponseEntity<>(dream, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create new dream
    @PostMapping
    public ResponseEntity<Dream> createDream(@RequestBody Dream dream) {
        try {
            Dream savedDream = dreamRepository.save(dream);
            return new ResponseEntity<>(savedDream, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update dream
    @PutMapping("/{id}")
    public ResponseEntity<Dream> updateDream(@PathVariable Long id, @RequestBody Dream dreamDetails) {
        Optional<Dream> dreamData = dreamRepository.findById(id);

        if (dreamData.isPresent()) {
            Dream dream = dreamData.get();
            dream.setDreamText(dreamDetails.getDreamText());
            dream.setMood(dreamDetails.getMood());
            dream.setLucidDream(dreamDetails.getLucidDream());
            dream.setSleepQuality(dreamDetails.getSleepQuality());
            dream.setTags(dreamDetails.getTags());

            return new ResponseEntity<>(dreamRepository.save(dream), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete dream
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteDream(@PathVariable Long id) {
        try {
            dreamRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get word cloud data
    @GetMapping("/word-cloud")
    public ResponseEntity<Map<String, Integer>> getWordCloud() {
        try {
            List<Dream> dreams = dreamRepository.findAll();
            Map<String, Integer> wordFrequency = new HashMap<>();

            // Common English stop words to filter out
            Set<String> stopWords = new HashSet<>(Arrays.asList(
                    "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
                    "of", "with", "by", "from", "as", "is", "was", "were", "been", "be",
                    "have", "has", "had", "do", "does", "did", "will", "would", "could",
                    "should", "may", "might", "can", "i", "you", "he", "she", "it", "we",
                    "they", "my", "your", "his", "her", "its", "our", "their", "me", "him",
                    "them", "this", "that", "these", "those", "am", "are", "then", "than",
                    "when", "where", "who", "what", "which", "how", "all", "each", "every",
                    "both", "few", "more", "most", "some", "such", "no", "nor", "not",
                    "only", "own", "same", "so", "just", "very", "too", "also"
            ));

            for (Dream dream : dreams) {
                if (dream.getDreamText() != null && !dream.getDreamText().isEmpty()) {
                    // Convert to lowercase and remove punctuation
                    String[] words = dream.getDreamText().toLowerCase()
                            .replaceAll("[^a-z\\s]", " ")
                            .split("\\s+");

                    for (String word : words) {
                        // Only include words longer than 3 characters and not in stop words
                        if (word.length() > 3 && !stopWords.contains(word)) {
                            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                        }
                    }
                }
            }

            // Sort by frequency and limit to top 50 words
            Map<String, Integer> sortedWordCloud = wordFrequency.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .limit(50)
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            return new ResponseEntity<>(sortedWordCloud, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get recurring themes
    @GetMapping("/themes")
    public ResponseEntity<Map<String, Long>> getRecurringThemes() {
        try {
            List<String> allTags = dreamRepository.findAllTags();
            Map<String, Long> themeCount = new HashMap<>();

            for (String tagString : allTags) {
                if (tagString != null && !tagString.isEmpty()) {
                    String[] tags = tagString.split(",");
                    for (String tag : tags) {
                        String trimmedTag = tag.trim();
                        if (!trimmedTag.isEmpty()) {
                            themeCount.put(trimmedTag, themeCount.getOrDefault(trimmedTag, 0L) + 1);
                        }
                    }
                }
            }

            // Sort by count descending
            Map<String, Long> sortedThemes = themeCount.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (e1, e2) -> e1,
                            LinkedHashMap::new
                    ));

            return new ResponseEntity<>(sortedThemes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get dreams by mood
    @GetMapping("/mood/{mood}")
    public ResponseEntity<List<Dream>> getDreamsByMood(@PathVariable String mood) {
        try {
            List<Dream> dreams = dreamRepository.findByMood(mood);
            return new ResponseEntity<>(dreams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get lucid dreams only
    @GetMapping("/lucid")
    public ResponseEntity<List<Dream>> getLucidDreams() {
        try {
            List<Dream> dreams = dreamRepository.findByLucidDreamTrue();
            return new ResponseEntity<>(dreams, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}