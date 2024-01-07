package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.MyViewHolder>{
    Context context;
    ArrayList<ExerciseTemplate> originalExercises; // Maintain the original list
    ArrayList<ExerciseTemplate> filteredExercises;
    private final SpremljeniTreninziAdapterInterface recyclerViewInterface;

    public HistoryRVAdapter(Context context, ArrayList<ExerciseTemplate> lexercises, SpremljeniTreninziAdapterInterface recyclerViewInterface) {
        this.context = context;
        this.originalExercises = new ArrayList<>(lexercises); // Save the original list
        this.filteredExercises = new ArrayList<>(lexercises); // Initialize filtered list with the same content
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public HistoryRVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.rv_history_row, parent, false);
        return new HistoryRVAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryRVAdapter.MyViewHolder holder, int position) {
        holder.tvHistoryName.setText(filteredExercises.get(position).getTemplateName());
        holder.tvHistoryDate.setText(convertDateFormat(filteredExercises.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return filteredExercises.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView tvHistoryName, tvHistoryDate;
        public MyViewHolder(@NonNull View itemView, SpremljeniTreninziAdapterInterface recyclerViewInterface) {
            super(itemView);
            tvHistoryDate = itemView.findViewById(R.id.tvHistoryDate);
            tvHistoryName = itemView.findViewById(R.id.tvHistoryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION) {
                            // Fetch item from filtered list based on the clicked position
                            ExerciseTemplate clickedExercise = filteredExercises.get(pos);
                            int originalPos = originalExercises.indexOf(clickedExercise);
                            recyclerViewInterface.onItemClick(originalPos);
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
                            ExerciseTemplate clickedExercise = filteredExercises.get(pos);
                            int originalPos = originalExercises.indexOf(clickedExercise);
                            recyclerViewInterface.onItemLongclick(originalPos);
                            // Update filtered list and notify adapter
                           // filteredExercises.remove(pos);
                            notifyItemRemoved(pos);
                            notifyItemRangeChanged(pos, getItemCount());
                            return true;
                        }
                    }
                    return false;
                }
            });
        }
    }
    public static String convertDateFormat(String inputDate) {
        String modifiedDate = inputDate.replace("-", ".");
        modifiedDate += ".";
        return modifiedDate;
    }
    public void filterList(ArrayList<ExerciseTemplate> filteredList) {
        filteredExercises = filteredList;
        notifyDataSetChanged();
    }

}
