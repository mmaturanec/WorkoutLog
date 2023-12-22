package com.example.workoutlog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PrijedloziTreningaFragment extends Fragment {

    FloatingActionButton btnAddTemplate;
    RecyclerView recyclerView;
    SpremljeniTreninziAdapter adapter;
    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_prijedlozi_treninga, container, false);

        btnAddTemplate = view.findViewById(R.id.btnAddTemplate);
        recyclerView = view.findViewById(R.id.rvSpremljeniTreninzi);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SpremljeniTreninziAdapter(getActivity(), exerciseTemplates);
        recyclerView.setAdapter(adapter);


        fetchExerciseTemplatesFromFirebase();

        btnAddTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewTemplateMain = new Intent(getActivity(), NewTemplateMain.class);
                startActivity(intentNewTemplateMain);

            }
        });
        return view;
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
}
