package com.example.workoutlog;

public class ExerciseSet {
    public String reps;
    public String weight;

    public ExerciseSet(String reps, String weight) {
        this.reps = reps;
        this.weight = weight;
    }

    public String getReps() {
        return reps;
    }

    public String getWeight() {
        return weight;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
