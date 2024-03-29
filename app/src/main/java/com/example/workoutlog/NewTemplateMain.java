package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTemplateMain extends AppCompatActivity implements NewTemplateMainAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener, PotvrdiBrisanjeTemplateDialog.DialogClickListener {
    private ArrayList<Exercise> lexercise;
    private Button btnDodajVjezbu;
    private Button btnSpremiTrening;
    private NewTemplateMainAdapter adapterRV;
    private TextInputLayout TILTemplate;
    private ImageView ivInfoButtonTemplate;
    private EditText tiTemplateName;
    private ImageView ivOdbaciTemplate;
    private String PreuzetiDatum;
    private ProgressBar progressBarTemplate;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_template_main);
        lexercise = (ArrayList<Exercise>) ExerciseSingleton.getInstance().getExercises();

        btnDodajVjezbu = findViewById(R.id.btnDodajVjezbu);
        recyclerView = findViewById(R.id.rvNewTemplateMain);
        ivInfoButtonTemplate = findViewById(R.id.ivInfoButtonTemplate);
        btnSpremiTrening = findViewById(R.id.btnSpremiTrening);
        tiTemplateName = findViewById(R.id.tiTemplateName);
        PreuzetiDatum = ExerciseSingleton.getInstance().getPreuzetiDatum();
        progressBarTemplate = findViewById(R.id.progressBarTemplate);
        TILTemplate = findViewById(R.id.TILTemplate);


        final Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {


            String type = bundle.getString("type");

            if(type.equals("editTemplate"))
            {
                ExerciseSingleton.getInstance().setSetImeTemplate(bundle.getString("templateName"));
                lexercise = bundle.getParcelableArrayList("exerciseList");
                ExerciseSingleton.getInstance().setNodeId(bundle.getString("nodeId"));
                ExerciseSingleton.getInstance().setLexercise(lexercise);
                ExerciseSingleton.getInstance().setEditingTemplate(true);

            }
            else if(type.equals("editWorkout"))
            {
                ExerciseSingleton.getInstance().setSetImeTemplate(bundle.getString("templateName"));
                lexercise = bundle.getParcelableArrayList("exerciseList");
                ExerciseSingleton.getInstance().setNodeId(bundle.getString("nodeId"));
                ExerciseSingleton.getInstance().setLexercise(lexercise);
                ExerciseSingleton.getInstance().setEditingWorkout(true);
                ExerciseSingleton.getInstance().setPreuzetiDatum(bundle.getString("datum"));
            }
            else if(type.equals("noviTrening")){
                ExerciseSingleton.getInstance().setSetImeTemplate(bundle.getString("templateName"));
                lexercise = bundle.getParcelableArrayList("exerciseList");
                ExerciseSingleton.getInstance().setNodeId(bundle.getString("nodeId"));
                ExerciseSingleton.getInstance().setLexercise(lexercise);
                ExerciseSingleton.getInstance().setPreuzetiDatum(bundle.getString("datum"));
                //PreuzetiDatum = bundle.getString("datum");


                ExerciseSingleton.getInstance().setNoviTrening(true);
            }
            else if(type.equals("noviTreningClean"))
            {
                ExerciseSingleton.getInstance().setPreuzetiDatum(bundle.getString("datum"));
                //PreuzetiDatum = bundle.getString("datum");


                ExerciseSingleton.getInstance().setNoviTrening(true);

            }

        }
        tiTemplateName.setText(ExerciseSingleton.getInstance().getSetImeTemplate());
        if(ExerciseSingleton.getInstance().getNoviTrening() == true)
        {
            btnSpremiTrening.setText(R.string.ZavrsiTrening);

        }
        Log.d("getDatum", "preuzeti datum " + ExerciseSingleton.getInstance().getPreuzetiDatum());

        adapterRV = new NewTemplateMainAdapter(this, lexercise, this);
        ivOdbaciTemplate = findViewById(R.id.ivOdbaciTemplate);
        recyclerView.setAdapter(adapterRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        ivOdbaciTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteTemplateDialog();
            }
        });
        ivInfoButtonTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoODodavanjuVjezbiDialog.showInfoDialog(NewTemplateMain.this);

            }
        });

        btnDodajVjezbu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseSingleton.getInstance().setSetImeTemplate(tiTemplateName.getText().toString());
                Intent intDodajVjezbu = new Intent(NewTemplateMain.this, DodajVjezbuTemplate.class);
                startActivity(intDodajVjezbu);
                overridePendingTransition(0, 0);

            }
        });




        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        btnSpremiTrening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String imeTemplate = tiTemplateName.getText().toString();
                String currentDate = new SimpleDateFormat("d-MM-yyyy", Locale.getDefault()).format(new Date());

                if(TextUtils.isEmpty(imeTemplate))
                {
                    Toast.makeText(NewTemplateMain.this, getString(R.string.UnesiTemplateIme), Toast.LENGTH_SHORT).show();

                }
                else if(lexercise.isEmpty())
                {
                    Toast.makeText(NewTemplateMain.this, getString(R.string.DodajBaremJednuVjezbu), Toast.LENGTH_SHORT).show();

                }
                else if(ExerciseSingleton.getInstance().getNoviTrening() == true)
                {
                    if (currentUser != null) {
                        String userUid = currentUser.getUid();
                        btnSpremiTrening.setVisibility(View.GONE);
                        btnDodajVjezbu.setVisibility(View.GONE);
                        ivInfoButtonTemplate.setVisibility(View.GONE);
                        TILTemplate.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        ivOdbaciTemplate.setVisibility(View.GONE);
                        progressBarTemplate.setVisibility(View.VISIBLE);
                        DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                                .child("user_exercise_history")
                                .child(userUid);

                        userExerciseTemplateRef.orderByChild("templateName").equalTo(imeTemplate).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    ExerciseTemplate exerciseTemplate = new ExerciseTemplate(imeTemplate,lexercise, ExerciseSingleton.getInstance().getPreuzetiDatum());

                                    String templateKey = userExerciseTemplateRef.push().getKey();
                                    if (templateKey != null) {
                                        userExerciseTemplateRef.child(templateKey).setValue(exerciseTemplate)
                                                .addOnSuccessListener(aVoid -> {
                                                    //ExerciseSingleton.destroyInstance();
                                                    Toast.makeText(NewTemplateMain.this, getString(R.string.uspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();
                                                    Intent intentMainmenu = new Intent(NewTemplateMain.this, WorkoutSummary.class);
                                                    intentMainmenu.putExtra("INITIAL_FRAGMENT", 0);
                                                    startActivity(intentMainmenu);
                                                    overridePendingTransition(0, 0);

                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(NewTemplateMain.this, getString(R.string.neuspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();

                                                });
                                    }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Handle potential database error
                            }
                        });
                    } else {
                        // User is not authenticated
                        // Handle this case as needed
                    }
                }
                else if(ExerciseSingleton.getInstance().getEditingTemplate() == true)
                {
                    btnSpremiTrening.setVisibility(View.GONE);
                    btnDodajVjezbu.setVisibility(View.GONE);
                    ivInfoButtonTemplate.setVisibility(View.GONE);
                    TILTemplate.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ivOdbaciTemplate.setVisibility(View.GONE);
                    progressBarTemplate.setVisibility(View.VISIBLE);
                    ExerciseTemplate exerciseTemplate = new ExerciseTemplate(imeTemplate,lexercise, currentDate);
                    String nodeId = ExerciseSingleton.getInstance().getNodeId();
                    updateExerciseTemplatesInDatabase(exerciseTemplate, nodeId);
                    ExerciseSingleton.destroyInstance();
                    Toast.makeText(NewTemplateMain.this, getString(R.string.uspjesnoAzuriranPredlozak), Toast.LENGTH_SHORT).show();

                    Intent intentMainmenu = new Intent(NewTemplateMain.this, MainMenuActivity.class);
                    intentMainmenu.putExtra("INITIAL_FRAGMENT", 1);
                    startActivity(intentMainmenu);
                    overridePendingTransition(0, 0);
                }
                else if(ExerciseSingleton.getInstance().getEditingWorkout())
                {
                    btnSpremiTrening.setVisibility(View.GONE);
                    btnDodajVjezbu.setVisibility(View.GONE);
                    ivInfoButtonTemplate.setVisibility(View.GONE);
                    TILTemplate.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    ivOdbaciTemplate.setVisibility(View.GONE);
                    progressBarTemplate.setVisibility(View.VISIBLE);
                    ExerciseTemplate exerciseTemplate = new ExerciseTemplate(imeTemplate, lexercise, ExerciseSingleton.getInstance().getPreuzetiDatum());
                    String nodeId = ExerciseSingleton.getInstance().getNodeId();
                    updateExerciseWorkoutInDatabase(exerciseTemplate, nodeId);
                    ExerciseSingleton.destroyInstance();
                    Toast.makeText(NewTemplateMain.this, getString(R.string.uspjesnoAzuriranPredlozak), Toast.LENGTH_SHORT).show();

                    Intent intentMainmenu = new Intent(NewTemplateMain.this, MainMenuActivity.class);
                    intentMainmenu.putExtra("INITIAL_FRAGMENT", 0);
                    startActivity(intentMainmenu);
                    overridePendingTransition(0, 0);
                }
                    else{
                        if (currentUser != null) {
                            btnSpremiTrening.setVisibility(View.GONE);
                            btnDodajVjezbu.setVisibility(View.GONE);
                            ivInfoButtonTemplate.setVisibility(View.GONE);
                            TILTemplate.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            ivOdbaciTemplate.setVisibility(View.GONE);
                            progressBarTemplate.setVisibility(View.VISIBLE);
                            String userUid = currentUser.getUid();

                            DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                                    .child("user_exercise_templates")
                                    .child(userUid);

                            userExerciseTemplateRef.orderByChild("templateName").equalTo(imeTemplate).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Toast.makeText(NewTemplateMain.this, getString(R.string.ImePredloskaVecPostojI), Toast.LENGTH_SHORT).show();

                                    } else {
                                        ExerciseTemplate exerciseTemplate = new ExerciseTemplate(imeTemplate,lexercise, currentDate);

                                        String templateKey = userExerciseTemplateRef.push().getKey();
                                        if (templateKey != null) {
                                            userExerciseTemplateRef.child(templateKey).setValue(exerciseTemplate)
                                                    .addOnSuccessListener(aVoid -> {
                                                        ExerciseSingleton.destroyInstance();
                                                        Toast.makeText(NewTemplateMain.this, getString(R.string.uspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();
                                                        Intent intentMainmenu = new Intent(NewTemplateMain.this, MainMenuActivity.class);
                                                        intentMainmenu.putExtra("INITIAL_FRAGMENT", 1);
                                                        startActivity(intentMainmenu);
                                                        overridePendingTransition(0, 0);

                                                    })
                                                    .addOnFailureListener(e -> {
                                                        Toast.makeText(NewTemplateMain.this, getString(R.string.neuspjesnoSpremljenPredlozak), Toast.LENGTH_SHORT).show();

                                                    });
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle potential database error
                                }
                            });
                        } else {
                            // User is not authenticated
                            // Handle this case as needed
                        }
                }




            }
        });
    }
    @Override
    public void onBackPressed() {
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
    private void showDeleteTemplateDialog() {
        PotvrdiBrisanjeTemplateDialog.showDialog(NewTemplateMain.this, new PotvrdiBrisanjeTemplateDialog.DialogClickListener() {
            @Override
            public void onNegativeButtonClick() {
                // Handle negative button click (if needed)
            }
        });
    }
    private void updateExerciseTemplatesInDatabase(ExerciseTemplate exerciseTemplate, String nodeId) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference exerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                .child("user_exercise_templates")
                .child(currentUser.getUid()) // Replace currentUser with your user reference
                .child(nodeId); // Provide the specific node ID to update

        exerciseTemplateRef.setValue(exerciseTemplate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // The node has been updated successfully
                        // Handle success as needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while updating
                    }
                });
    }
    private void updateExerciseWorkoutInDatabase(ExerciseTemplate exerciseTemplate, String nodeId) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        DatabaseReference exerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                .child("user_exercise_history")
                .child(currentUser.getUid()) // Replace currentUser with your user reference
                .child(nodeId); // Provide the specific node ID to update

        exerciseTemplateRef.setValue(exerciseTemplate)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // The node has been updated successfully
                        // Handle success as needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that occurred while updating
                    }
                });
    }
}