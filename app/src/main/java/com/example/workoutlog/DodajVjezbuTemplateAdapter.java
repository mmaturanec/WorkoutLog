package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DodajVjezbuTemplateAdapter extends RecyclerView.Adapter<DodajVjezbuTemplateAdapter.MyViewHolder>{
    Context context;
    ArrayList<ExerciseSet> lexercise;

    public ArrayList<ExerciseSet> getAllExerciseSets() {
        return lexercise;
    }
    public void updateExerciseSetValues(ArrayList<String> updatedReps, ArrayList<String> updatedWeights) {
        for (int i = 0; i < lexercise.size(); i++) {
            ExerciseSet set = lexercise.get(i);
            if (i < updatedReps.size() && i < updatedWeights.size()) {
                set.setReps(updatedReps.get(i));
                set.setWeight(updatedWeights.get(i));
            }
        }
        notifyDataSetChanged();
    }

    public DodajVjezbuTemplateAdapter(Context context, ArrayList<ExerciseSet> lexercise) {
        this.context = context;
        this.lexercise = lexercise;
    }
    @NonNull
    @Override
    public DodajVjezbuTemplateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_dodaj_vjezbu_template_row, parent, false);
        return new DodajVjezbuTemplateAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DodajVjezbuTemplateAdapter.MyViewHolder holder, int position) {
        ExerciseSet exerciseSet = lexercise.get(position);

        // Check if EditTexts are null before setting text
        if (exerciseSet != null) {
            if (holder.etBrojPonavljanja != null && holder.etRadnaKilaza != null) {
                holder.etBrojPonavljanja.setText(exerciseSet.getReps() != null ? exerciseSet.getReps() : "");
                holder.etRadnaKilaza.setText(exerciseSet.getWeight() != null ? exerciseSet.getWeight() : "");
            }
        }
    }




    public int getItemCount() {
        return lexercise.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDeleteSet;
        EditText etBrojPonavljanja;
        EditText etRadnaKilaza;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            etBrojPonavljanja = itemView.findViewById(R.id.etBrojPonavljanja);
            etRadnaKilaza = itemView.findViewById(R.id.etRadnaKilaza);

            ivDeleteSet = itemView.findViewById(R.id.ivDeleteSet);
            ivDeleteSet = itemView.findViewById(R.id.ivDeleteSet);

            ivDeleteSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        deleteRow(position);

                    }
                }
            });
        }
    }
    public void addNewRow() {
        lexercise.add(new ExerciseSet("", ""));
        notifyItemInserted(lexercise.size() - 1);

    }
    public void deleteRow(int position) {
        lexercise.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lexercise.size());
    }
    public int getRowCount() {
        return lexercise.size();
    }

}
