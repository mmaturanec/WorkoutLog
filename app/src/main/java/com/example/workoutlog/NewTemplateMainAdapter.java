package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewTemplateMainAdapter extends RecyclerView.Adapter<NewTemplateMainAdapter.MyViewHolder> {
    Context context;
    ArrayList<Exercise> lexercise;
    private final NewTemplateMainAdapterInterface recyclerViewInterface;

    public NewTemplateMainAdapter(Context context, ArrayList<Exercise> lexercise, NewTemplateMainAdapterInterface recyclerViewInterface) {
        this.context = context;
        this.lexercise = lexercise;
        this.recyclerViewInterface = recyclerViewInterface;

    }

    @NonNull
    @Override
    public NewTemplateMainAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_new_template_main_row, parent, false);
        return new NewTemplateMainAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NewTemplateMainAdapter.MyViewHolder holder, int position) {
        holder.tvSetImeVjezbe.setText(lexercise.get(position).getExerciseName());

        List<ExerciseSet> exerciseSets = lexercise.get(position).getExerciseSets();

        // Initialize and set up the inner RecyclerView
        RecyclerView innerRecyclerView = holder.innerRecyclerView;
        SingleVjezbaSetsAdapter innerAdapter = new SingleVjezbaSetsAdapter(context, exerciseSets);
        innerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        innerRecyclerView.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return lexercise.size();
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSetImeVjezbe;
        RecyclerView innerRecyclerView;
        ImageView ivEditVjezba;

        public MyViewHolder(@NonNull View itemView, NewTemplateMainAdapterInterface recyclerViewInterface) {
            super(itemView);
            tvSetImeVjezbe = itemView.findViewById(R.id.tvSetImeVjezbe);
            innerRecyclerView = itemView.findViewById(R.id.rvSingleVjezbaTemplate);
            ivEditVjezba = itemView.findViewById(R.id.ivEditVjezba);

            ivEditVjezba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos); // Assuming you have a method for edit click in the interface
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemLongclick(pos); // Assuming you have a method for long click deletion in the interface
                            notifyItemRemoved(pos);
                            return true; // Return true to indicate the long click has been consumed
                        }
                    }
                    return false; // Return false if the long click action was not handled
                }
            });
        }

    }

}
