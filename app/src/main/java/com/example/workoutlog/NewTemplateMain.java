package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class NewTemplateMain extends AppCompatActivity implements NewTemplateMainAdapterInterface {
    private ArrayList<Exercise> lexercise;
    private Button btnDodajVjezbu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_template_main);
        lexercise = (ArrayList<Exercise>) ExerciseSingleton.getInstance().getExercises();
        btnDodajVjezbu = findViewById(R.id.btnDodajVjezbu);
        RecyclerView recyclerView = findViewById(R.id.rvNewTemplateMain);

        NewTemplateMainAdapter adapterRV = new NewTemplateMainAdapter(this, lexercise, this);
        recyclerView.setAdapter(adapterRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        btnDodajVjezbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intDodajVjezbu = new Intent(NewTemplateMain.this, DodajVjezbuTemplate.class);
                startActivity(intDodajVjezbu);
                overridePendingTransition(0, 0);

            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(NewTemplateMain.this, DodajVjezbuTemplate.class);

        intent.putExtra("imeVjezbe", lexercise.get(position).getExerciseName());
        intent.putExtra("setoviVjezbe", lexercise.get(position).getExerciseSets());
        intent.putExtra("position", position);
        startActivity(intent);
        overridePendingTransition(0, 0);

    }
}