package com.example.workoutlog;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ExerciseTemplate {

    private String TemplateName;
    private ArrayList<Exercise> lexercise;
    private String Date;
    private String nodeId;
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
    @Exclude // Exclude this field from being saved in Firebase
    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }


}
