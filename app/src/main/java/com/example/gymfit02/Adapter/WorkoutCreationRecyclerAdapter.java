package com.example.gymfit02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Models.DatabaseExerciseModel;
import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class WorkoutCreationRecyclerAdapter extends FirestoreRecyclerAdapter<DatabaseExerciseModel, WorkoutCreationRecyclerAdapter.ViewHolder> {


    private OnItemClickListener listener;


    public WorkoutCreationRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DatabaseExerciseModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout_creation_exercise_single, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull DatabaseExerciseModel model) {
        holder.getExercise_name().setText(model.getExerciseName());
        holder.getDevice_name().setText(model.getDeviceName());
        holder.getIcon_check().setVisibility(View.GONE);
        // clicked.set(position, holder.getIcon_check().getVisibility());
        // VISIBLE == 0
        // GONE == 8
        holder.getIcon_check().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int visibility = holder.getIcon_check().getVisibility();

                if(visibility == View.VISIBLE) {
                    holder.getIcon_check().setVisibility(View.GONE);
                } else {
                    holder.getIcon_check().setVisibility(View.VISIBLE);
                }
            }
        });


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
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView exercise_name;
        private TextView device_name;
        private ImageView icon_check;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            exercise_name = itemView.findViewById(R.id.listItem_workout_creation_exercise_name);
            device_name = itemView.findViewById(R.id.listItem_workout_creation_device_name);
            icon_check = itemView.findViewById(R.id.imageView_workout_creation_check);

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

        public TextView getExercise_name() {
            return exercise_name;
        }

        public TextView getDevice_name() {
            return device_name;
        }

        public ImageView getIcon_check() {
            return icon_check;
        }

        // SETTER


        public void setExercise_name(TextView exercise_name) {
            this.exercise_name = exercise_name;
        }

        public void setDevice_name(TextView device_name) {
            this.device_name = device_name;
        }

        public void setIcon_check(ImageView icon_check) {
            this.icon_check = icon_check;
        }
    }

}


