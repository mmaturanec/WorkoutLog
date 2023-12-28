package com.example.workoutlog;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSingleton {
    private static ExerciseSingleton oInstance = null;
    private ArrayList<Exercise> lexercise = new ArrayList<>();
    private String SetImeTemplate = new String();
    private Boolean editingTemplate = false;
    private Boolean noviTrening = false;
    private Boolean editingWorkout = false;
    private String nodeId = new String();



    private String preuzetiDatum = new String();
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

    public void setLexercise(ArrayList<Exercise> lexercise) {
        this.lexercise = lexercise;
    }

    public Boolean getEditingTemplate() {
        return editingTemplate;
    }

    public void setEditingTemplate(Boolean editingTemplate) {
        this.editingTemplate = editingTemplate;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public void setNoviTrening(Boolean noviTrening) {
        this.noviTrening = noviTrening;
    }

    public Boolean getNoviTrening() {
        return noviTrening;
    }
    public String getPreuzetiDatum() {
        return preuzetiDatum;
    }

    public void setPreuzetiDatum(String preuzetiDatum) {
        this.preuzetiDatum = preuzetiDatum;
    }
    public Boolean getEditingWorkout() {
        return editingWorkout;
    }

    public void setEditingWorkout(Boolean editingWorkout) {
        this.editingWorkout = editingWorkout;
    }
}
