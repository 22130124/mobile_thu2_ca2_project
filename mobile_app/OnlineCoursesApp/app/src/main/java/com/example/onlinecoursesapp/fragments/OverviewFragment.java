package com.example.onlinecoursesapp.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlinecoursesapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class OverviewFragment extends Fragment {

    // UI components
    private TextView tvTotalStudents;
    private TextView tvTotalCourses;
    private TextView tvTotalHours;
    private TextView tvCompletionRate;
    private TextView tvNewToday;
    private TextView tvNewWeek;
    private LineChart registrationChart;
    private PieChart studentStatusChart;
    private RecyclerView newCoursesRecyclerView;
    private RecyclerView completedLessonsRecyclerView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI components
        initViews(view);

        // Setup charts
        setupCharts();

        // Load data
        loadStatisticsData();
        loadChartData();

        if (newCoursesRecyclerView != null) {
            newCoursesRecyclerView.setVisibility(View.GONE);
        }

        if (completedLessonsRecyclerView != null) {
            completedLessonsRecyclerView.setVisibility(View.GONE);
        }
    }

    private void initViews(View view) {
        // Statistics TextViews
        tvTotalStudents = view.findViewById(R.id.tv_total_students);
        tvTotalCourses = view.findViewById(R.id.tv_total_courses);
        tvTotalHours = view.findViewById(R.id.tv_total_hours);
        tvCompletionRate = view.findViewById(R.id.tv_completion_rate);
        tvNewToday = view.findViewById(R.id.tv_new_today);
        tvNewWeek = view.findViewById(R.id.tv_new_week);

        // Charts
        registrationChart = view.findViewById(R.id.chart_registrations);
        studentStatusChart = view.findViewById(R.id.chart_student_status);

        // RecyclerViews (we'll disable these)
        newCoursesRecyclerView = view.findViewById(R.id.rv_new_courses);
        completedLessonsRecyclerView = view.findViewById(R.id.rv_completed_lessons);
    }

    private void setupCharts() {
        // Setup Registration Chart
        registrationChart.getDescription().setEnabled(false);
        registrationChart.setTouchEnabled(true);
        registrationChart.setDragEnabled(true);
        registrationChart.setScaleEnabled(true);
        registrationChart.setPinchZoom(true);
        registrationChart.setDrawGridBackground(false);

        XAxis xAxis = registrationChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);

        // Setup Student Status Chart
        studentStatusChart.getDescription().setEnabled(false);
        studentStatusChart.setUsePercentValues(true);
        studentStatusChart.setDrawHoleEnabled(true);
        studentStatusChart.setHoleColor(Color.WHITE);
        studentStatusChart.setHoleRadius(58f);
        studentStatusChart.setTransparentCircleRadius(61f);
        studentStatusChart.setDrawCenterText(true);
        studentStatusChart.setCenterText("Students");
        studentStatusChart.setRotationEnabled(true);
        studentStatusChart.setHighlightPerTapEnabled(true);
    }

    private void loadStatisticsData() {
        // In a real app, these would come from a database or API
        tvTotalStudents.setText("1,234");
        tvTotalCourses.setText("42");
        tvTotalHours.setText("560");
        tvCompletionRate.setText("78%");
        tvNewToday.setText("24");
        tvNewWeek.setText("156");
    }

    private void loadChartData() {
        // Load Registration Chart Data
        ArrayList<Entry> registrationEntries = new ArrayList<>();
        registrationEntries.add(new Entry(0, 45));
        registrationEntries.add(new Entry(1, 32));
        registrationEntries.add(new Entry(2, 38));
        registrationEntries.add(new Entry(3, 42));
        registrationEntries.add(new Entry(4, 56));
        registrationEntries.add(new Entry(5, 68));
        registrationEntries.add(new Entry(6, 75));

        LineDataSet registrationDataSet = new LineDataSet(registrationEntries, "Weekly Registrations");
        registrationDataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        registrationDataSet.setLineWidth(2f);
        registrationDataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        registrationDataSet.setCircleRadius(4f);
        registrationDataSet.setDrawCircleHole(false);
        registrationDataSet.setValueTextSize(10f);
        registrationDataSet.setDrawFilled(true);
        registrationDataSet.setFillColor(getResources().getColor(android.R.color.holo_blue_light));

        ArrayList<String> xLabels = new ArrayList<>();
        xLabels.add("Mon");
        xLabels.add("Tue");
        xLabels.add("Wed");
        xLabels.add("Thu");
        xLabels.add("Fri");
        xLabels.add("Sat");
        xLabels.add("Sun");

        registrationChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        registrationChart.setData(new LineData(registrationDataSet));
        registrationChart.invalidate();

        // Load Student Status Chart Data
        ArrayList<PieEntry> statusEntries = new ArrayList<>();
        statusEntries.add(new PieEntry(65, "In Progress"));
        statusEntries.add(new PieEntry(25, "Completed"));
        statusEntries.add(new PieEntry(10, "Not Started"));

        PieDataSet statusDataSet = new PieDataSet(statusEntries, "");
        statusDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        statusDataSet.setValueTextColor(Color.WHITE);
        statusDataSet.setValueTextSize(14f);

        studentStatusChart.setData(new PieData(statusDataSet));
        studentStatusChart.invalidate();
    }
}