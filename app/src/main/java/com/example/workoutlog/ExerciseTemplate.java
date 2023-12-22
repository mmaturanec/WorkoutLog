package com.example.workoutlog;

import java.util.ArrayList;

public class ExerciseTemplate {

    private String TemplateName;
    private ArrayList<Exercise> lexercise;
    private String Date;
    public ExerciseTemplate() {

    }

    public ExerciseTemplate(String TemplateName, ArrayList<Exercise> lexercise, String date) {
        this.TemplateName = TemplateName;
        this.lexercise = lexercise;
        Date = date;
    }

    public ArrayList<Exercise> getLexercise() {
        return lexercise;
    }


    public String getDate() {
        return Date;
    }

    public void setLexercise(ArrayList<Exercise> lexercise) {
        this.lexercise = lexercise;
    }



    public void setDate(String date) {
        Date = date;
    }

    public String getTemplateName() {
        return TemplateName;
    }

    public void setTemplateName(String templateName) {
        TemplateName = templateName;
    }
}
