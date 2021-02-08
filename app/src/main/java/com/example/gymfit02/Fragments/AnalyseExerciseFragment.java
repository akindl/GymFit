package com.example.gymfit02.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.gymfit02.Models.DatabaseExercisePerformanceModel;
import com.example.gymfit02.Models.DatabaseExercisePerformanceSetModel;
import com.example.gymfit02.R;
import com.example.gymfit02.Util.Container;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Function;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AnalyseExerciseFragment extends Fragment {

    private static final String TAG = "analyseExerciseFragment";

    // Firestore Connection
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userId;
    private FirebaseUser user;

    // Bundle
    String exerciseId = "TbqQ25iccZgpm7AAnUa0";

    public AnalyseExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the workoutId from the selected Workout
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            this.exerciseId = bundle.getString("exerciseId");
        }

        setupFirestoreConnection();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analyse_exercise, container, false);
        getActivity().setTitle("Analyse von: ");

        final LineChart oneRepMaxLineChart = rootView.findViewById(R.id.lineChart_analyseExercise_oneRepMax);
        final LineChart totalVolumeLineChart = rootView.findViewById(R.id.lineChart_analyseExercise_totalVolume);

        OnSuccessListener<ArrayList<Entry>> oneRepMaxDataListener = new OnSuccessListener<ArrayList<Entry>>() {
            @Override
            public void onSuccess(ArrayList<Entry> entries) {
                setupChart(oneRepMaxLineChart, entries, "One Rep Max", "kg");
            }
        };

        OnSuccessListener<ArrayList<Entry>> totalVolumeDataListener = new OnSuccessListener<ArrayList<Entry>>() {
            @Override
            public void onSuccess(ArrayList<Entry> entries) {
                setupChart(totalVolumeLineChart, entries, "Total Volume", "kg");
            }
        };
        this.loadChartDataFromFirebase(oneRepMaxDataListener, totalVolumeDataListener);
        return rootView;
    }

    /**
     * Help-method to connect to Firestore
     */
    private void setupFirestoreConnection() {
        // Connect to Firebase user
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        userId = fAuth.getCurrentUser().getUid();
        fStore = FirebaseFirestore.getInstance();
    }

    private void setupChart(final LineChart chart, ArrayList<Entry> values, String name, final String yAxisUnit) {
        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setBackgroundColor(Color.argb(50,78,101,120));
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);


        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        // xAxis.setTypeface(tfLight);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(true); // NEW from false
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM");
                return format.format(new Date((long) value * 1000));
            }
        });


        YAxis yAxis = chart.getAxisLeft();
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        // leftAxis.setTypeface(tfLight);
        yAxis.setYOffset(-7f);
        yAxis.setDrawGridLines(true);
        yAxis.setGranularityEnabled(true);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf(value + " " +  yAxisUnit);
            }
        });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);

        // Data
        LineDataSet set1 = new LineDataSet(values, name);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.enableDashedLine(10f,10f, 10f);
        set1.setCubicIntensity(0.2f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(Color.rgb(113,152,47)); // NEW ColorTemplate.getHoloBlue()
        // set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(3f);
        set1.setDrawValues(true); // NEW
        // set1.setFillAlpha(100);
        set1.setFillColor(Color.rgb(113,152,47)); // NEW ColorTemplate.getHoloBlue()
        set1.setHighLightColor(Color.rgb(244,117,117));
        set1.setDrawCircleHole(false);

        set1.setDrawCircles(true);
        set1.setCircleRadius(5f);
        set1.setCircleColor(Color.rgb(113,152,47));

        // create a data object with the data sets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(11f);

        // set data
        chart.setData(data);
        chart.invalidate();
    }

    private void loadChartDataFromFirebase(
            final OnSuccessListener<ArrayList<Entry>> oneRepMaxDataListener,
            final OnSuccessListener<ArrayList<Entry>> totalVolumeDataListener) {

        fStore.collection("Users").document(userId)
                .collection("Exercises").document(exerciseId)
                .collection("ExercisePerformances")
                .whereGreaterThanOrEqualTo("setCount", 1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot data) {
                        ArrayList<Entry> maxOneRepData = new ArrayList<>();
                        ArrayList<Entry> totalVolumeData = new ArrayList<>();

                        for (DocumentSnapshot performanceSetSnapshot : data) {
                            DatabaseExercisePerformanceModel performanceSetModel = performanceSetSnapshot.toObject(DatabaseExercisePerformanceModel.class);
                            maxOneRepData.add(new Entry(performanceSetModel.getPerformanceDate().getSeconds(), performanceSetModel.getOneRepMax()));
                            totalVolumeData.add(new Entry(performanceSetModel.getPerformanceDate().getSeconds(), performanceSetModel.getTotalVolume()));
                        }
                        oneRepMaxDataListener.onSuccess(maxOneRepData);
                        totalVolumeDataListener.onSuccess(totalVolumeData);
                    }
                });
    }
}






























