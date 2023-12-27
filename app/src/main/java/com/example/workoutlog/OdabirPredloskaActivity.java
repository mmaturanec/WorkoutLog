package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OdabirPredloskaActivity extends AppCompatActivity implements SpremljeniTreninziAdapterInterface{

    RecyclerView recyclerView;
    SpremljeniTreninziAdapter adapter;
    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();
    Button btnPovratakNaMainMenu;
    boolean hideEditRV = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odabir_predloska);

        recyclerView = findViewById(R.id.rvSpremljeniTreninzi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SpremljeniTreninziAdapter(this, exerciseTemplates, this, true);
        recyclerView.setAdapter(adapter);


        fetchExerciseTemplatesFromFirebase();

        btnPovratakNaMainMenu = findViewById(R.id.btnPovratakNaMainMenu);

        btnPovratakNaMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMainMenu = new Intent(OdabirPredloskaActivity.this, MainMenuActivity.class);
                startActivity(intentMainMenu);
                overridePendingTransition(0, 0);
            }
        });

    }
    private void fetchExerciseTemplatesFromFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                .child("user_exercise_templates")
                .child(currentUser.getUid()); // Replace currentUser with your user reference

        // Assuming you want to fetch all templates
        userExerciseTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                exerciseTemplates.clear(); // Clear the list to avoid duplicates
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ExerciseTemplate exerciseTemplate = snapshot.getValue(ExerciseTemplate.class);
                    if (exerciseTemplate != null) {
                        exerciseTemplate.setNodeId(snapshot.getKey());
                        exerciseTemplates.add(exerciseTemplate);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onItemClick(int position) {

        final Bundle bundle = getIntent().getExtras();
        String date = bundle.getString("datum");
        Log.d("getDatum", "saljem: "+date);
        Intent intent = new Intent(OdabirPredloskaActivity.this, NewTemplateMain.class);

        intent.putExtra("templateName", exerciseTemplates.get(position).getTemplateName());
        intent.putParcelableArrayListExtra("exerciseList", exerciseTemplates.get(position).getLexercise());
        intent.putExtra("nodeId", exerciseTemplates.get(position).getNodeId());
        intent.putExtra("type", "noviTrening");
        intent.putExtra("datum", date);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onItemLongclick(int position) {

    }
}