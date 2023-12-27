package com.example.workoutlog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpremljeniTreninziAdapter extends RecyclerView.Adapter<SpremljeniTreninziAdapter.MyViewHolder>{
    Context context;
    ArrayList<ExerciseTemplate> lexercise;
    boolean hideEditRV;
    private final SpremljeniTreninziAdapterInterface recyclerViewInterface;

    public SpremljeniTreninziAdapter(Context context, ArrayList<ExerciseTemplate> lexercise, SpremljeniTreninziAdapterInterface recyclerViewInterface, boolean hideEditRV ) {
        this.context = context;
        this.lexercise = lexercise;
        this.recyclerViewInterface = recyclerViewInterface;
        this.hideEditRV = hideEditRV;
    }

    @NonNull
    @Override
    public SpremljeniTreninziAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_rvspremljeni_treninzi_row, parent, false);
        return new SpremljeniTreninziAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SpremljeniTreninziAdapter.MyViewHolder holder, int position) {

        holder.tvImeTemplate.setText(lexercise.get(position).getTemplateName());
        ImageView ivEditTemplateST = holder.itemView.findViewById(R.id.ivEditTemplateST);

        if (hideEditRV) {
            ivEditTemplateST.setImageResource(R.drawable.baseline_add_circle_24);
        }
    }

    @Override
    public int getItemCount() {
        return lexercise.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivEditTemplateST;
        TextView tvImeTemplate;
        public MyViewHolder(@NonNull View itemView, SpremljeniTreninziAdapterInterface recyclerViewInterface) {
            super(itemView);

            tvImeTemplate = itemView.findViewById(R.id.tvImeTemplate);
            ivEditTemplateST = itemView.findViewById(R.id.ivEditTemplateST);

            ivEditTemplateST.setOnClickListener(new View.OnClickListener() {
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
}
