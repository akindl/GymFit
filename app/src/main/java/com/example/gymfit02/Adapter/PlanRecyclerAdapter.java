package com.example.gymfit02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.gymfit02.Models.DatabasePlanModel;
import com.example.gymfit02.R;
import com.example.gymfit02.Util.PlanViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PlanRecyclerAdapter extends FirestoreRecyclerAdapter<DatabasePlanModel, PlanViewHolder> {

    public PlanRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DatabasePlanModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_single, parent, false);
        return new PlanViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PlanViewHolder holder, int position, @NonNull DatabasePlanModel model) {
        holder.getPlan_name().setText(model.getName());
        holder.getPlan_numberOfExercises().setText(model.getNumberOfExercises());
        holder.getPlan_numberOfSets().setText(model.getNumberOfSets());
        holder.getPlan_repetitionRange().setText(model.getRepetitionRange());
        holder.getPlan_totalVolume().setText(model.getTotalVolume());
    }
}
