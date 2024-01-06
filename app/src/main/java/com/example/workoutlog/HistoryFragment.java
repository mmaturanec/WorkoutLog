package com.example.workoutlog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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


public class HistoryFragment extends Fragment implements SpremljeniTreninziAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener{

    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();
    ImageView iwSortHistory;
    RecyclerView recyclerView;
    HistoryRVAdapter adapter;
    TextView tvNemaTreningaHistory;
    EditText etSearchHistory;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        iwSortHistory = view.findViewById(R.id.iwSortHistory);
        tvNemaTreningaHistory = view.findViewById(R.id.tvNemaTreningaHistory);
        etSearchHistory = view.findViewById(R.id.etSearchHistory);
        recyclerView = view.findViewById(R.id.rvHistory);
        adapter = new HistoryRVAdapter(getContext(), exerciseTemplates, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchExerciseTemplatesFromFirebase();

        etSearchHistory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase().trim();
                filterExerciseTemplates(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used
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
        return view;
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
        adapter.notifyDataSetChanged();
        fetchExerciseTemplatesFromFirebase();
    }

    @Override
    public void onNegativeButtonClick() {
        PotvrdiBrisanjeVjezbeDialog.dismissDeleteExerciseDialog();

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), NewTemplateMain.class);

        intent.putExtra("templateName", exerciseTemplates.get(position).getTemplateName());
        intent.putParcelableArrayListExtra("exerciseList", exerciseTemplates.get(position).getLexercise());
        intent.putExtra("datum", exerciseTemplates.get(position).getDate());
        intent.putExtra("nodeId", exerciseTemplates.get(position).getNodeId());
        intent.putExtra("type", "editWorkout");
        startActivity(intent);
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
                    .child("user_exercise_history")
                    .child(currentUser.getUid()) // Replace currentUser with your user reference
                    .child(nodeId); // Provide the specific node ID to delete

            nodeRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), getResources().getString(R.string.uspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), getResources().getString(R.string.neuspjesnoBrisanjeTemplate), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
        }
    }
    private void filterExerciseTemplates(String searchText) {
        ArrayList<ExerciseTemplate> filteredList = new ArrayList<>();

        for (ExerciseTemplate template : exerciseTemplates) {
            if (template.getTemplateName().toLowerCase().contains(searchText)) {
                filteredList.add(template);
            }
        }

        if (filteredList.isEmpty()) {
            tvNemaTreningaHistory.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNemaTreningaHistory.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.filterList(filteredList);
    }

}