package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewTemplateMain extends AppCompatActivity implements NewTemplateMainAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener  {
    private ArrayList<Exercise> lexercise;
    private Button btnDodajVjezbu;
    private NewTemplateMainAdapter adapterRV;
    private ImageView ivInfoButtonTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_template_main);
        lexercise = (ArrayList<Exercise>) ExerciseSingleton.getInstance().getExercises();
        btnDodajVjezbu = findViewById(R.id.btnDodajVjezbu);
        RecyclerView recyclerView = findViewById(R.id.rvNewTemplateMain);
        ivInfoButtonTemplate = findViewById(R.id.ivInfoButtonTemplate);

        adapterRV = new NewTemplateMainAdapter(this, lexercise, this);
        recyclerView.setAdapter(adapterRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ivInfoButtonTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoODodavanjuVjezbiDialog.showInfoDialog(NewTemplateMain.this);

            }
        });

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

    @Override
    public void onItemLongclick(final int position) {
        PotvrdiBrisanjeVjezbeDialog.showDeleteExerciseDialog(this, this, position);

    }

    @Override
    public void onPositiveButtonClick(int position) {
        ExerciseSingleton.getInstance().removeAtPosition(position);
        adapterRV.notifyItemRemoved(position);
        Toast.makeText(this, "Vježba uspješno obrisana!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNegativeButtonClick() {
        PotvrdiBrisanjeVjezbeDialog.dismissDeleteExerciseDialog();

    }
}