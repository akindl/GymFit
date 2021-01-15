package com.example.gymfit02.Util;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.R;

public class PlanViewHolder extends RecyclerView.ViewHolder{

    private TextView plan_name;
    private TextView plan_numberOfExercises;
    private TextView plan_numberOfSets;
    private TextView plan_repetitionRange;
    private TextView plan_totalVolume;


    public PlanViewHolder(@NonNull View itemView) {
        super(itemView);

        plan_name = itemView.findViewById(R.id.listItem_plan_name);
        plan_numberOfExercises = itemView.findViewById(R.id.listItem_plan_numberOfExercises);
        plan_numberOfSets = itemView.findViewById(R.id.listItem_plan_numberOfSets);
        plan_repetitionRange = itemView.findViewById(R.id.listItem_plan_repetition_range);
        plan_totalVolume = itemView.findViewById(R.id.listItem_plan_total_volume);
    }


    // GETTER

    public TextView getPlan_name() {
        return plan_name;
    }

    public TextView getPlan_numberOfExercises() {
        return plan_numberOfExercises;
    }

    public TextView getPlan_numberOfSets() {
        return plan_numberOfSets;
    }

    public TextView getPlan_repetitionRange() {
        return plan_repetitionRange;
    }

    public TextView getPlan_totalVolume() {
        return plan_totalVolume;
    }


    // SETTER

    public void setPlan_name(TextView plan_name) {
        this.plan_name = plan_name;
    }

    public void setPlan_numberOfExercises(TextView plan_numberOfExercises) {
        this.plan_numberOfExercises = plan_numberOfExercises;
    }

    public void setPlan_numberOfSets(TextView plan_numberOfSets) {
        this.plan_numberOfSets = plan_numberOfSets;
    }

    public void setPlan_repetitionRange(TextView plan_repetitionRange) {
        this.plan_repetitionRange = plan_repetitionRange;
    }

    public void setPlan_totalVolume(TextView plan_totalVolume) {
        this.plan_totalVolume = plan_totalVolume;
    }
}
