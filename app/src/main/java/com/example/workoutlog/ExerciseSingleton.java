package com.example.workoutlog;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSingleton {
    private static ExerciseSingleton oInstance = null;
    private ArrayList<Exercise> lexercise = new ArrayList<>();
    protected ExerciseSingleton() {
    }
    public static ExerciseSingleton getInstance() {
        if(oInstance == null) {
            oInstance = new ExerciseSingleton();
        }
        return oInstance;
    }
    public List<Exercise> getExercises()
    {
        return lexercise;
    }
    public  void addExercise(Exercise student)
    {
        lexercise.add(student);
    }
    public void removeAtPosition(int position) {
        if (position >= 0 && position < lexercise.size()) {
            lexercise.remove(position);
        }
    }
    public void addAtPosition(Exercise exercise, int position) {
        if (position >= 0 && position <= lexercise.size()) {
            lexercise.add(position, exercise);
        }
    }
}