package com.example.workoutlog;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class NoviTreningFragment extends Fragment implements SpremljeniTreninziAdapterInterface, PotvrdiBrisanjeVjezbeDialog.DialogClickListener{
    String selectedDate;
    TextView tvTreninziNaDan;
    String PrikazDatuma;
    RecyclerView recyclerView;
    SpremljeniTreninziAdapter adapter;
    ArrayList<ExerciseTemplate> exerciseTemplates = new ArrayList<>();
    TextView tvNemaTreningaZaPrikaz;
    ImageView ivGetHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novi_trening, container, false);

        OdabirNovogTreningaDialogAdapter dialogAdapter = new OdabirNovogTreningaDialogAdapter(requireActivity());
        CalendarView calendarView = view.findViewById(R.id.calendarView);
        Button btnNoviTreningDialog = view.findViewById(R.id.btnNoviTreningDialog);
        tvNemaTreningaZaPrikaz = view.findViewById(R.id.tvNemaTreningaZaPrikaz);
        long dateInMillis = calendarView.getDate();
         selectedDate = convertMillisToDate(dateInMillis);


        Calendar calendar = Calendar.getInstance();
        long maxDate = calendar.getTimeInMillis();
        calendarView.setMaxDate(maxDate);


        ivGetHistory = view.findViewById(R.id.ivGetHistory);
        ivGetHistory.setOnClickListener(v ->{
            Intent intent = new Intent(requireActivity(), HistoryActivity.class);
            startActivity(intent);
        });
        tvTreninziNaDan = view.findViewById(R.id.tvTreninziNaDan);
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.M.yyyy.", Locale.getDefault());
        SimpleDateFormat dateFormatBaza = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault());
        selectedDate = dateFormatBaza.format(today);

      
        PrikazDatuma = dateFormat.format(today);
        tvTreninziNaDan.setText(getString(R.string.TreninziNaDan) + " " + PrikazDatuma);


        recyclerView = view.findViewById(R.id.rvHistory);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SpremljeniTreninziAdapter(getActivity(), exerciseTemplates, this, false);
        recyclerView.setAdapter(adapter);


        fetchExerciseHistoryFromFirebaseByDate(selectedDate);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                    String  curDate = String.valueOf(dayOfMonth);
                    String  Year = String.valueOf(year);
                    String  Month = String.valueOf(month+1);

                    selectedDate = curDate+"-"+Month+"-"+Year;
                    PrikazDatuma = curDate+"."+Month+"."+Year+".";
                tvTreninziNaDan.setText(getString(R.string.TreninziNaDan) + " " + PrikazDatuma);
                fetchExerciseHistoryFromFirebaseByDate(selectedDate);
                if(exerciseTemplates.isEmpty())
                {
                    tvNemaTreningaZaPrikaz.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    tvNemaTreningaZaPrikaz.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });

        btnNoviTreningDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selectedDateMillis = calendarView.getDate();
                dialogAdapter.showDialog(selectedDate);
            }
        });

        return view;
    }

    private void fetchExerciseHistoryFromFirebaseByDate(String date) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference userExerciseTemplateRef = FirebaseDatabase.getInstance().getReference()
                .child("user_exercise_history")
                .child(currentUser.getUid());

        // Create a query to filter by 'date'
        Query query = userExerciseTemplateRef.orderByChild("date").equalTo(date);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    tvNemaTreningaZaPrikaz.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else{
                    tvNemaTreningaZaPrikaz.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


    private String convertMillisToDate(long millis) {
        // Convert millis to date string here (you can use SimpleDateFormat or other methods)
        // This is just an example, implement your conversion logic
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    @Override
    public void onPositiveButtonClick(int position) {
        String nodeId = exerciseTemplates.get(position).getNodeId();
        deleteNodeFromDatabase(nodeId);
        fetchExerciseHistoryFromFirebaseByDate(selectedDate);

    }

    @Override
    public void onNegativeButtonClick() {
        PotvrdiBrisanjeVjezbeDialog.dismissDeleteExerciseDialog();

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), NewTemplateMain.class);

        intent.putExtra("templateName", exerciseTemplates.get(position).getTemplateName());
        intent.putParcelableArrayListExtra("exerciseList", exerciseTemplates.get(position).getLexercise());
        intent.putExtra("datum", selectedDate);
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

}