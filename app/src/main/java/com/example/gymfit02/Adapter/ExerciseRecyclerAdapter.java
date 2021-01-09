package com.example.gymfit02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_database_exercise_single, parent, false);
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





/*
public class ExerciseListAdapter extends RecyclerView.Adapter<ExerciseListAdapter.ViewHolder> {

    private String[] localDataSet;

    */
/**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     *//*

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            // textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            //return textView;
        }
    }

    */
/**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     *//*

    public ExerciseListAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}*/
