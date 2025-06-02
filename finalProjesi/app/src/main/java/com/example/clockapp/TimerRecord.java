package com.example.clockapp;

public class TimerRecord {
    private int hours;
    private int minutes;
    private String timestamp;

    public TimerRecord(int hours, int minutes, String timestamp) {
        this.hours = hours;
        this.minutes = minutes;
        this.timestamp = timestamp;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getTimestamp() {
        return timestamp;
    }
}