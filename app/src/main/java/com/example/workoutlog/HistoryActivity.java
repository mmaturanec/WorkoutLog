package com.example.workoutlog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity implements SpremljeniTreninziAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener{
    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();
    ImageView iwSortHistory, iwInfoHistory;
    Button btnPovratakHistory;
    RecyclerView recyclerView;
    HistoryRVAdapter adapter;
    TextView tvNemaTreningaHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        iwSortHistory = findViewById(R.id.iwSortHistory);
        iwInfoHistory = findViewById(R.id.iwInfoHistory);
        btnPovratakHistory = findViewById(R.id.btnHistoryPovratak);
        tvNemaTreningaHistory = findViewById(R.id.tvNemaTreningaHistory);

        btnPovratakHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainMenu = new Intent(HistoryActivity.this, MainMenuActivity.class);
                startActivity(mainMenu);
                overridePendingTransition(0, 0);
            }
        });
         recyclerView = findViewById(R.id.rvHistory);
         adapter = new HistoryRVAdapter(this, exerciseTemplates, this);
         recyclerView.setAdapter(adapter);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchExerciseTemplatesFromFirebase();

        iwInfoHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoHistoryDialog.showInfoDialog(HistoryActivity.this);

            }
        });
    }
    private void fetchExerciseTemplatesFromFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                .child("user_exercise_history")
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
                sortExerciseTemplatesByDate();
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
        iwSortHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exerciseTemplates.isEmpty())
                {
                    tvNemaTreningaHistory.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    tvNemaTreningaHistory.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    Collections.reverse(exerciseTemplates);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
    private void sortExerciseTemplatesByDate() {
        if(exerciseTemplates.isEmpty())
        {
            tvNemaTreningaHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            tvNemaTreningaHistory.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            Comparator<ExerciseTemplate> dateComparator = new Comparator<ExerciseTemplate>() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-M-yyyy");

                @Override
                public int compare(ExerciseTemplate exercise1, ExerciseTemplate exercise2) {
                    try {
                        Date date1 = dateFormat.parse(exercise1.getDate());
                        Date date2 = dateFormat.parse(exercise2.getDate());
                        // Compare dates in reverse order for descending sorting
                        return date2.compareTo(date1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        return 0; // Handle parse exception as needed
                    }
                }
            };

            // Sort the exerciseTemplates using the custom comparator
            Collections.sort(exerciseTemplates, dateComparator);
        }

    }

    @Override
    public void onPositiveButtonClick(int position) {
        String nodeId = exerciseTemplates.get(position).getNodeId();
        deleteNodeFromDatabase(nodeId);
        fetchExerciseTemplatesFromFirebase();
    }

    @Override
    public void onNegativeButtonClick() {
        PotvrdiBrisanjeVjezbeDialog.dismissDeleteExerciseDialog();

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, NewTemplateMain.class);

        intent.putExtra("templateName", exerciseTemplates.get(position).getTemplateName());
        intent.putParcelableArrayListExtra("exerciseList", exerciseTemplates.get(position).getLexercise());
        intent.putExtra("datum", exerciseTemplates.get(position).getDate());
        intent.putExtra("nodeId", exerciseTemplates.get(position).getNodeId());
        intent.putExtra("type", "editWorkout");
        startActivity(intent);
    }

    @Override
    public void onItemLongclick(int position) {
        PotvrdiBrisanjeVjezbeDialog.showDeleteExerciseDialog(this, this, position);

    }
    private void deleteNodeFromDatabase(String nodeId) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference nodeRef = FirebaseDatabase.getInstance().getReference()
                    .child("user_exercise_history")
                    .child(currentUser.getUid()) // Replace currentUser with your user reference
                    .child(nodeId); // Provide the specific node ID to delete

            nodeRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(HistoryActivity.this, getResources().getString(R.string.uspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HistoryActivity.this, getResources().getString(R.string.neuspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
        }
    }
}