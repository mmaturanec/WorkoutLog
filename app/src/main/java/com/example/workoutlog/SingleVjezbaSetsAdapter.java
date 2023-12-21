package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SingleVjezbaSetsAdapter extends RecyclerView.Adapter<SingleVjezbaSetsAdapter.MyViewHolder> {

    Context context;
    List<ExerciseSet> lexercise;

    public SingleVjezbaSetsAdapter(Context context, List<ExerciseSet> lexercise) {
        this.context = context;
        this.lexercise = lexercise;
    }

    @NonNull
    @Override
    public SingleVjezbaSetsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.singlev_vjezba_template_row, parent, false);
        return new SingleVjezbaSetsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleVjezbaSetsAdapter.MyViewHolder holder, int position) {
        holder.tvSingleWeight.setText(lexercise.get(position).getWeight());
        holder.tvSingleReps.setText(lexercise.get(position).getReps());
    }

    @Override
    public int getItemCount() {
        return lexercise.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSingleWeight, tvSingleReps;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSingleWeight = itemView.findViewById(R.id.tvSingleWeight);
            tvSingleReps = itemView.findViewById(R.id.tvSingleReps);
        }

    }
}
