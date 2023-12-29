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
    ArrayList<ExerciseTemplate> lexercises;
    private final SpremljeniTreninziAdapterInterface recyclerViewInterface;

    public HistoryRVAdapter(Context context, ArrayList<ExerciseTemplate> lexercises, SpremljeniTreninziAdapterInterface recyclerViewInterface) {
        this.context = context;
        this.lexercises = lexercises;
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
        holder.tvHistoryName.setText(lexercises.get(position).getTemplateName());
        holder.tvHistoryDate.setText(convertDateFormat(lexercises.get(position).getDate()));

    }

    @Override
    public int getItemCount() {
        return lexercises.size();
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
                            notifyItemRangeChanged(pos, getItemCount()); // Notify adapter of data change
                            return true; // Return true to indicate the long click has been consumed
                        }
                    }
                    return false; // Return false if the long click action was not handled
                }
            });
        }
    }
    public static String convertDateFormat(String inputDate) {
        String modifiedDate = inputDate.replace("-", ".");
        modifiedDate += ".";
        return modifiedDate;
    }
}
