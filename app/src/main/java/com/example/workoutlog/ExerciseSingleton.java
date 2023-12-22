package com.example.workoutlog;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSingleton {
    private static ExerciseSingleton oInstance = null;
    private ArrayList<Exercise> lexercise = new ArrayList<>();
    private String SetImeTemplate = new String();
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

    public String getSetImeTemplate() {
        return SetImeTemplate;
    }

    public void setSetImeTemplate(String setImeTemplate) {
        SetImeTemplate = setImeTemplate;
    }
    public static void destroyInstance() {
        oInstance = null;
    }
}
