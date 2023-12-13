package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class DodajVjezbuTemplate extends AppCompatActivity {
    private Button btnDodajVjezbu;
    private Button btnDodajSeriju;
    private EditText etImeVjezbeTemplate;
    private TextView tvBrojSerija;
    private ArrayList<ExerciseSet> lexercise = new ArrayList<>();
    private ArrayList<ExerciseSet> exerciseSets = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dodaj_vjezbu_template);
        btnDodajVjezbu = findViewById(R.id.btnSpremiVjezbuTemplate);
        etImeVjezbeTemplate = findViewById(R.id.etImeVjezbeTemplate);
        btnDodajSeriju = findViewById(R.id.btnDodajSeriju);
        tvBrojSerija = findViewById(R.id.tvUkupanBrojSerija);
        RecyclerView recyclerView = findViewById(R.id.rvDodajVjezbu);
        lexercise.add(new ExerciseSet("", ""));
        DodajVjezbuTemplateAdapter adapterRV = new DodajVjezbuTemplateAdapter(this, lexercise);
        recyclerView.setAdapter(adapterRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvBrojSerija.setText(""+lexercise.size());


        adapterRV.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                // Update the row count when the entire dataset changes
                int rowCount = adapterRV.getItemCount();
                tvBrojSerija.setText(String.valueOf(rowCount));

            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                // Update the row count when new items are inserted
                int rowCount = adapterRV.getItemCount();
                tvBrojSerija.setText(String.valueOf(rowCount));

            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                // Update the row count when items are removed
                int rowCount = adapterRV.getItemCount();
                tvBrojSerija.setText(String.valueOf(rowCount));

            }
        });

        btnDodajSeriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterRV.addNewRow();
            }
        });
        btnDodajSeriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // tvBrojSerija.setText(""+(lexercise.size()+1));
                adapterRV.addNewRow();
            }

        });



        btnDodajVjezbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseSets = adapterRV.getAllExerciseSets();
                ArrayList<String> updatedReps = new ArrayList<>();
                ArrayList<String> updatedWeights = new ArrayList<>();
                for (int i = 0; i < lexercise.size(); i++) {
                    View itemView = recyclerView.getLayoutManager().findViewByPosition(i);
                    EditText etReps = itemView.findViewById(R.id.etBrojPonavljanja);
                    EditText etWeight = itemView.findViewById(R.id.etRadnaKilaza);

                    String reps = etReps.getText().toString();
                    String weight = etWeight.getText().toString();

                    updatedReps.add(reps);
                    updatedWeights.add(weight);
                }

                // Update the adapter with the collected values and refresh RecyclerView
                adapterRV.updateExerciseSetValues(updatedReps, updatedWeights);
                Log.d("listasetova", "Lista:");
                Log.d("listasetova", "velicina: "+exerciseSets.size());
                for (ExerciseSet set : exerciseSets) {
                    Log.d("listasetova", "Reps: " + set.reps + ", Weight: " + set.weight);
                }
                }
                String nazivVjezbe = etImeVjezbeTemplate.getText().toString();

            //TODO: Dodaj validaciju na unose i onda promjeni onaj row (dodaj mu neki prikaz liste vjv recycler viewer jos jedan to ti je najlakse prema instanci) i implementiraj nakraju prosirenje instance
               // Intent intNewTemp = new Intent(DodajVjezbuTemplate.this, NewTemplateMain.class);
                //ExerciseSingleton.getInstance().addExercise(new Exercise(etImeVjezbeTemplate.getText().toString()));
                //startActivity(intNewTemp);

        });

    }

}