package com.example.jaron.accelerometer;

public class Workout {
    private String id, date, exercise;

    public Workout() {}

    public Workout(String id, String date, String exercise) {
        this.id = id;
        this.date = date;
        this.exercise = exercise;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }
    public String getExercise() {
        return exercise;
    }

    public void setId(String s) {
        id = s;
    }

    public void setDate(String s) {
        date = s;
    }

    public void setExercise(String s) {
        exercise = s;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", excercises='" + exercise + '\'' +
                '}';
    }
}

