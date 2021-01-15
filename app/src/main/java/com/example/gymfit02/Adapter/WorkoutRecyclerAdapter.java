package com.example.gymfit02.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Models.DatabaseWorkoutModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


public class WorkoutRecyclerAdapter extends FirestoreRecyclerAdapter<DatabaseWorkoutModel, WorkoutRecyclerAdapter.WorkoutViewHolder> {


    private OnItemClickListener listener;

    public WorkoutRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DatabaseWorkoutModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_single, parent, false);
        return new WorkoutViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkoutViewHolder holder, final int position, @NonNull DatabaseWorkoutModel model) {
        holder.getWorkout_name().setText(model.getName());
        holder.getWorkout_executionDate().setText(model.getExecutionDate());
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


    /**
     * With this Listener we can send data from the adapter to the underlying activity/fragment
     * that implements this interface
     */
    public interface OnItemClickListener {
        // From the documentSnapchat we can recreate the whole object, the unique id of the firestore
        // document and we can get the documentReference to make changes
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onItemLongClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }



    /**
     * ViewHolder Class
     */
    class WorkoutViewHolder extends RecyclerView.ViewHolder {

        private TextView workout_name;
        private TextView workout_executionDate;


        public WorkoutViewHolder(@NonNull final View itemView) {
            super(itemView);

            workout_name = itemView.findViewById(R.id.listItem_workout_name);
            workout_executionDate = itemView.findViewById(R.id.listItem_workout_executionDate);

            // item click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    // send Click from the Adapter to the activity/ fragment
                    // NO_POSITION is constant = -1
                    if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(adapterPosition), adapterPosition);

                    }
                }
            });

            // item long click
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int adapterPosition = getAdapterPosition();
                    // send Click from the Adapter to the activity/ fragment
                    if (adapterPosition != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(getSnapshots().getSnapshot(adapterPosition), adapterPosition);
                    }
                    return true;
                }
            });
        }


        // GETTER

        public TextView getWorkout_name() {
            return workout_name;
        }

        public TextView getWorkout_executionDate() {
            return workout_executionDate;
        }



        // SETTER

        public void setWorkout_name(TextView workout_name) {
            this.workout_name = workout_name;
        }

        public void setWorkout_executionDate(TextView workout_executionDate) {
            this.workout_executionDate = workout_executionDate;
        }


    }

}

