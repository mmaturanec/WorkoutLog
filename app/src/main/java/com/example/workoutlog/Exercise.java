package com.example.workoutlog;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private String exerciseName;
    private ArrayList<ExerciseSet> exerciseSets;

    public Exercise()
    {

    }


    public Exercise(String exerciseName, ArrayList<ExerciseSet> exerciseSets) {
        this.exerciseName = exerciseName;
        this.exerciseSets = exerciseSets;

    }

    public String getExerciseName() {
        return exerciseName;
    }

    public ArrayList<ExerciseSet> getExerciseSets() {
        return exerciseSets;
    }


}
