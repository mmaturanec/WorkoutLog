package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutSummary extends AppCompatActivity {

    private Button btnPovratakGlavniMeni;
    private ArrayList<Exercise> lexercise;
    private TextView tvUkupnoKilaze, tvUkupnoSerija, tvUkupnoPon, tvVjPodKil, tvvjNajvisePon;
    private float totalWeightLifted = 0;
    private int totalSetsCount = 0;
    private int totalRepsCount = 0;
    private String maxTotalWeightLifted, maxTotalReps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_summary);

        btnPovratakGlavniMeni = findViewById(R.id.btnPovratakGlavniMeni);
        lexercise = (ArrayList<Exercise>) ExerciseSingleton.getInstance().getExercises();
        tvUkupnoKilaze = findViewById(R.id.tvUkupnoKilaze);
        tvUkupnoSerija = findViewById(R.id.tvUkupnoSerija);
        tvUkupnoPon = findViewById(R.id.tvUkupnoPon);
        tvVjPodKil = findViewById(R.id.tvVjPodKil);
        tvvjNajvisePon = findViewById(R.id.tvvjNajvisePon);

        CalculateTotalWeightLifted();
        CalculateTotalSets();
        getTotalReps();

        tvUkupnoKilaze.setText(""+totalWeightLifted);
        tvUkupnoSerija.setText(""+totalSetsCount);
        tvUkupnoPon.setText(""+totalRepsCount);
        tvVjPodKil.setText(getExerciseWithMaxWeight());
        tvvjNajvisePon.setText(getExerciseWithMaxReps());


        btnPovratakGlavniMeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseSingleton.destroyInstance();
                Intent mainMenu = new Intent(WorkoutSummary.this, MainMenuActivity.class);
                startActivity(mainMenu);
                overridePendingTransition(0, 0);
            }
        });
    }
    @Override
    public void onBackPressed() {
    }
    private void CalculateTotalWeightLifted()
    {
        for (Exercise exercise : lexercise) {
            ArrayList<ExerciseSet> exerciseSets = exercise.getExerciseSets();

            for (ExerciseSet exerciseSet : exerciseSets) {
                int reps = Integer.parseInt(exerciseSet.getReps());
                float weight = Float.parseFloat(exerciseSet.getWeight());
                totalWeightLifted += reps * weight;
            }
        }
    }
    private void CalculateTotalSets()
    {
        for (Exercise exercise : lexercise) {
            ArrayList<ExerciseSet> exerciseSets = exercise.getExerciseSets();
            totalSetsCount += exerciseSets.size();
        }
    }
    public String getExerciseWithMaxWeight() {
        float maxWeightLifted = Float.MIN_VALUE;
        String exerciseNameWithMaxWeight = "";

        for (Exercise exercise : lexercise) {
            float totalWeightLiftedForExercise = 0;

            ArrayList<ExerciseSet> exerciseSets = exercise.getExerciseSets();

            for (ExerciseSet exerciseSet : exerciseSets) {
                int reps = Integer.parseInt(exerciseSet.getReps());
                float weight = Float.parseFloat(exerciseSet.getWeight());
                totalWeightLiftedForExercise += reps * weight;
            }

            // Check if this exercise has a higher total weight lifted than the current maximum
            if (totalWeightLiftedForExercise > maxWeightLifted) {
                maxWeightLifted = totalWeightLiftedForExercise;
                exerciseNameWithMaxWeight = exercise.getExerciseName();
            }
        }

        return exerciseNameWithMaxWeight;
    }
    public String getExerciseWithMaxReps() {
        int maxTotalReps = Integer.MIN_VALUE; // Initialize with the smallest possible value
        String exerciseNameWithMaxReps = "";

        for (Exercise exercise : lexercise) {
            int totalRepsForExercise = 0;

            ArrayList<ExerciseSet> exerciseSets = exercise.getExerciseSets();

            for (ExerciseSet exerciseSet : exerciseSets) {
                int reps = Integer.parseInt(exerciseSet.getReps());
                totalRepsForExercise += reps;
            }

            // Check if this exercise has a higher total reps than the current maximum
            if (totalRepsForExercise > maxTotalReps) {
                maxTotalReps = totalRepsForExercise;
                exerciseNameWithMaxReps = exercise.getExerciseName();
            }
        }

        return exerciseNameWithMaxReps;
    }
    public void getTotalReps() {
        int totalReps = 0;

        for (Exercise exercise : lexercise) {
            ArrayList<ExerciseSet> exerciseSets = exercise.getExerciseSets();

            for (ExerciseSet exerciseSet : exerciseSets) {
                int reps = Integer.parseInt(exerciseSet.getReps());
                totalReps += reps;
            }
        }

        totalRepsCount =  totalReps;
    }


}