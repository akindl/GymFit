package com.example.gymfit02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.R;
import com.example.gymfit02.Util.ExerciseViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ExerciseRecyclerAdapter extends FirestoreRecyclerAdapter<DatabaseExerciseModel, ExerciseViewHolder> {


    public ExerciseRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DatabaseExerciseModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_single, parent, false);
        return new ExerciseViewHolder(view);
        }

    @Override
    protected void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position, @NonNull DatabaseExerciseModel model) {
        holder.getExercise_name().setText(model.getName());
        holder.getExercise_numberOfSets().setText(model.getNumberOfSets());
        holder.getExercise_volume().setText(model.getVolume());
        holder.getExercise_maxLoad().setText(model.getMaxLoad());
        holder.getExercice_notes().setText(model.getNotes());

        }
}

