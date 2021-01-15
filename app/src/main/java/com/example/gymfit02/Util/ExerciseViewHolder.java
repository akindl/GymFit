package com.example.gymfit02.Util;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.R;

public class ExerciseViewHolder extends RecyclerView.ViewHolder {

    private TextView exercise_name;
    private TextView exercise_numberOfSets;
    private TextView exercise_volume;
    private TextView exercise_maxLoad;
    private TextView exercice_notes;

    public ExerciseViewHolder(@NonNull View itemView) {
        super(itemView);

        exercise_name = itemView.findViewById(R.id.listItem_plan_name);
        exercise_numberOfSets = itemView.findViewById(R.id.listItem_plan_numberOfExercises);
        exercise_volume = itemView.findViewById(R.id.listItem_plan_numberOfSets);
        exercise_maxLoad = itemView.findViewById(R.id.listItem_plan_repetition_range);
        exercice_notes = itemView.findViewById(R.id.listItem_dbExercise_notes);
    }


    // GETTER

    public TextView getExercise_name() {
        return exercise_name;
    }

    public TextView getExercise_numberOfSets() {
        return exercise_numberOfSets;
    }

    public TextView getExercise_volume() {
        return exercise_volume;
    }

    public TextView getExercise_maxLoad() {
        return exercise_maxLoad;
    }

    public TextView getExercice_notes() {
        return exercice_notes;
    }



    // SETTER

    public void setExercice_notes(TextView exercice_notes) {
        this.exercice_notes = exercice_notes;
    }

    public void setExercise_name(TextView exercise_name) {
        this.exercise_name = exercise_name;
    }
}
