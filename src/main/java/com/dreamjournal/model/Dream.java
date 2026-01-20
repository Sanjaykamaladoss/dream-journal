package com.dreamjournal.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dreams")
public class Dream {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String dreamText;

    @Column(length = 50)
    private String mood;

    @Column(name = "lucid_dream")
    private Boolean lucidDream;

    @Column(name = "sleep_quality")
    private Integer sleepQuality;

    @Column(columnDefinition = "TEXT")
    private String tags;

    @Column(nullable = false)
    private LocalDateTime date;

    // Constructors
    public Dream() {
        this.date = LocalDateTime.now();
        this.lucidDream = false;
    }

    public Dream(String dreamText, String mood, Boolean lucidDream, Integer sleepQuality, String tags) {
        this.dreamText = dreamText;
        this.mood = mood;
        this.lucidDream = lucidDream;
        this.sleepQuality = sleepQuality;
        this.tags = tags;
        this.date = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDreamText() {
        return dreamText;
    }

    public void setDreamText(String dreamText) {
        this.dreamText = dreamText;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public Boolean getLucidDream() {
        return lucidDream;
    }

    public void setLucidDream(Boolean lucidDream) {
        this.lucidDream = lucidDream;
    }

    public Integer getSleepQuality() {
        return sleepQuality;
    }

    public void setSleepQuality(Integer sleepQuality) {
        this.sleepQuality = sleepQuality;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Dream{" +
                "id=" + id +
                ", dreamText='" + dreamText + '\'' +
                ", mood='" + mood + '\'' +
                ", lucidDream=" + lucidDream +
                ", sleepQuality=" + sleepQuality +
                ", tags='" + tags + '\'' +
                ", date=" + date +
                '}';
    }
}
