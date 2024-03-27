package com.example.finalproject2nd.activities;

import java.util.Map;

public class Poll {
    private String id;
    private String question;
    private Map<String, Integer> options;

    // Constructor, getters, and setters

    public Poll() {
        // Default constructor required for Firebase
    }

    public Poll(String id, String question, Map<String, Integer> options) {
        this.id = id;
        this.question = question;
        this.options = options;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Map<String, Integer> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Integer> options) {
        this.options = options;
    }
}
