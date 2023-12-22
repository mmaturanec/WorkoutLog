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

    public SpremljeniTreninziAdapter(Context context, ArrayList<ExerciseTemplate> lexercise) {
        this.context = context;
        this.lexercise = lexercise;
    }

    @NonNull
    @Override
    public SpremljeniTreninziAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_rvspremljeni_treninzi_row, parent, false);
        return new SpremljeniTreninziAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpremljeniTreninziAdapter.MyViewHolder holder, int position) {

        holder.tvImeTemplate.setText(lexercise.get(position).getTemplateName());
    }

    @Override
    public int getItemCount() {
        return lexercise.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvImeTemplate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvImeTemplate = itemView.findViewById(R.id.tvImeTemplate);
        }
    }
}
