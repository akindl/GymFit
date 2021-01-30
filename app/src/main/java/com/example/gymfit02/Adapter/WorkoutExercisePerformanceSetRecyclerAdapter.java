package com.example.gymfit02.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymfit02.Models.DatabaseExercisePerformanceSetModel;
import com.example.gymfit02.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;



public class WorkoutExercisePerformanceSetRecyclerAdapter extends FirestoreRecyclerAdapter<DatabaseExercisePerformanceSetModel, WorkoutExercisePerformanceSetRecyclerAdapter.ExercisePerformanceSetViewHolder> {


    private OnItemClickListener listener;

    public WorkoutExercisePerformanceSetRecyclerAdapter(@NonNull FirestoreRecyclerOptions<DatabaseExercisePerformanceSetModel> options) {
        super(options);
    }


    @NonNull
    @Override
    public ExercisePerformanceSetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_set_row_single, parent, false);
        return new ExercisePerformanceSetViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ExercisePerformanceSetViewHolder holder,
                                    final int position,
                                    @NonNull DatabaseExercisePerformanceSetModel model) {

        // holder.getSetNumber().setText(String.valueOf(model.getSetNumber()));
        holder.getSetReps().setText(String.valueOf(model.getReps()));
        holder.getSetLoad().setText(String.valueOf(model.getLoad()));
        holder.getSetRpe().setText(model.getRpe());
        holder.getCheck().setChecked(model.getCheck());

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
    class ExercisePerformanceSetViewHolder extends RecyclerView.ViewHolder {

        private TextView setNumber;
        private TextView setLoad;
        private TextView setReps;
        private TextView setRpe;
        private RadioButton check;


        public ExercisePerformanceSetViewHolder(@NonNull final View itemView) {
            super(itemView);

            // setNumber = itemView.findViewById(R.id.textView_workoutExercisePerformanceSingleSet_number);
            setReps = itemView.findViewById(R.id.textView_workoutExercisePerformanceSingleSet_reps);
            setLoad = itemView.findViewById(R.id.textView_workoutExercisePerformanceSingleSet_load);
            setRpe = itemView.findViewById(R.id.textView_workoutExercisePerformanceSingleSet_rpe);
            check = itemView.findViewById(R.id.radioButton_workoutExercisePerformanceSingleSet_check);


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


        public TextView getSetLoad() {
            return setLoad;
        }

        public TextView getSetReps() {
            return setReps;
        }

        public TextView getSetRpe() {
            return setRpe;
        }

        public RadioButton getCheck() {
            return check;
        }



        // SETTER

        public void setSetLoad(TextView setLoad) {
            this.setLoad = setLoad;
        }

        public void setSetReps(TextView setReps) {
            this.setReps = setReps;
        }

        public void setSetRpe(TextView setRpe) {
            this.setRpe = setRpe;
        }

        public void setCheck(RadioButton check) {
            this.check = check;
        }
    }

}