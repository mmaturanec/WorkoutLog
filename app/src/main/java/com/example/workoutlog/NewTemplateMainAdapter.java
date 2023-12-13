package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewTemplateMainAdapter extends RecyclerView.Adapter<NewTemplateMainAdapter.MyViewHolder> {
    Context context;
    ArrayList<Exercise> lexercise;

    public NewTemplateMainAdapter(Context context, ArrayList<Exercise> lexercise) {
        this.context = context;
        this.lexercise = lexercise;
    }

    @NonNull
    @Override
    public NewTemplateMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_new_template_main_row, parent, false);
        return new NewTemplateMainAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTemplateMainAdapter.MyViewHolder holder, int position) {
        holder.tvSetImeVjezbe.setText(lexercise.get(position).getExerciseName());
    }

    @Override
    public int getItemCount() {
        return lexercise.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSetImeVjezbe;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSetImeVjezbe = itemView.findViewById(R.id.tvSetImeVjezbe);
        }
    }
}
