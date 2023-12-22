package com.example.workoutlog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DodajVjezbuTemplate extends AppCompatActivity {
    private Button btnDodajVjezbu;
    private Button btnDodajSeriju;
    private EditText etImeVjezbeTemplate;
    private TextView tvBrojSerija;
    private int position;
    private ImageView ivOdustaniDVT;
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
        ivOdustaniDVT = findViewById(R.id.ivOdustaniDVT);

        final Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            etImeVjezbeTemplate.setText(bundle.getString("imeVjezbe"));
            lexercise = bundle.getParcelableArrayList("setoviVjezbe");

            position = bundle.getInt("position");

        }
        else{
            lexercise.add(new ExerciseSet("", ""));
          }


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

                String nazivVjezbe = etImeVjezbeTemplate.getText().toString();
                if(TextUtils.isEmpty(nazivVjezbe))
                {
                    Toast.makeText(DodajVjezbuTemplate.this, getResources().getString(R.string.UnesiImeVjezbe), Toast.LENGTH_SHORT).show();

                }
                else if(checkForEmptySet(exerciseSets))
                {
                    Toast.makeText(DodajVjezbuTemplate.this, getResources().getString(R.string.UnesiteVrijednostiZaSvakuSeriju), Toast.LENGTH_SHORT).show();

                }
                else{

                        Intent intNewTemp = new Intent(DodajVjezbuTemplate.this, NewTemplateMain.class);
                    if(bundle != null)
                    {
                        ExerciseSingleton.getInstance().removeAtPosition(position);
                        ExerciseSingleton.getInstance().addAtPosition(new Exercise(nazivVjezbe, exerciseSets), position);
                    }
                    else{
                        ExerciseSingleton.getInstance().addExercise(new Exercise(nazivVjezbe, exerciseSets));
                    }
                        startActivity(intNewTemp);
                        overridePendingTransition(0, 0);
                    }


                }




                });//zavrsetakOnClick
        ivOdustaniDVT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        }
    private static boolean checkForEmptySet(List<ExerciseSet> exerciseSets) {
        for (ExerciseSet exerciseSet : exerciseSets) {
            if (exerciseSet.getReps().isEmpty() || exerciseSet.getWeight().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}