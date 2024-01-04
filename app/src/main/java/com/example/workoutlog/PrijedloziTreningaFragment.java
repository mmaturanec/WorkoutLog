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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class PrijedloziTreningaFragment extends Fragment implements SpremljeniTreninziAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener{

    FloatingActionButton btnAddTemplate;
    RecyclerView recyclerView;
    SpremljeniTreninziAdapter adapter;
    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();
    TextView tvNemaSpremljenihPredlozaka;


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
        tvNemaSpremljenihPredlozaka = view.findViewById(R.id.tvNemaSpremljenihPredlozaka);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SpremljeniTreninziAdapter(getActivity(), exerciseTemplates, this, false);
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
                        exerciseTemplate.setNodeId(snapshot.getKey());
                        exerciseTemplates.add(exerciseTemplate);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                if(exerciseTemplates.isEmpty())
                {
                    tvNemaSpremljenihPredlozaka.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    tvNemaSpremljenihPredlozaka.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), NewTemplateMain.class);

        intent.putExtra("templateName", exerciseTemplates.get(position).getTemplateName());
        intent.putParcelableArrayListExtra("exerciseList", exerciseTemplates.get(position).getLexercise());
        intent.putExtra("nodeId", exerciseTemplates.get(position).getNodeId());
        intent.putExtra("type", "editTemplate");
        startActivity(intent);
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onItemLongclick(int position) {
        PotvrdiBrisanjeVjezbeDialog.showDeleteExerciseDialog(getContext(), this, position);



    }
    private void deleteNodeFromDatabase(String nodeId) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference nodeRef = FirebaseDatabase.getInstance().getReference()
                    .child("user_exercise_templates")
                    .child(currentUser.getUid()) // Replace currentUser with your user reference
                    .child(nodeId); // Provide the specific node ID to delete

            nodeRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.uspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.neuspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
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
}
