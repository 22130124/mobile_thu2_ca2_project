package com.example.onlinecoursesapp.fragments.admin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecoursesapp.R;
import com.example.onlinecoursesapp.data.DashboardRepository;
import com.example.onlinecoursesapp.models.dashboard.DashboardStats;
import com.example.onlinecoursesapp.models.dashboard.RegistrationData;
import com.example.onlinecoursesapp.models.dashboard.StudentStatusData;
import com.example.onlinecoursesapp.utils.DashboardCallback;
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
import java.util.List;

//Kieu- Overview cho phần dashboard
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
    private DashboardRepository dashboardRepository;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize repository
        dashboardRepository = DashboardRepository.getInstance();
        // Initialize UI components
        initViews(view);
        // Setup charts
        setupCharts();
        // Load data from API
        loadDashboardStats();
        loadRegistrationData();
        loadStudentStatusData();
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
        studentStatusChart.setEntryLabelColor(Color.TRANSPARENT);
    }

    private void loadDashboardStats() {
        dashboardRepository.fetchDashboardStats(new DashboardCallback.DashboardStatsCallback() {
            @Override
            public void onSuccess(DashboardStats stats) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    tvTotalStudents.setText(String.valueOf(stats.getTotalStudents()));
                    tvTotalCourses.setText(String.valueOf(stats.getTotalCourses()));
                    tvTotalHours.setText(String.valueOf(stats.getTotalHours()));
                    tvCompletionRate.setText(String.format("%.1f%%", stats.getCompletionRate()));
                    tvNewToday.setText(String.valueOf(stats.getNewToday()));
                    tvNewWeek.setText(String.valueOf(stats.getNewWeek()));
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void loadRegistrationData() {
        dashboardRepository.fetchRegistrationData(new DashboardCallback.RegistrationDataCallback() {
            @Override
            public void onSuccess(List<RegistrationData.DailyRegistration> data) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> updateRegistrationChart(data));
            }
            @Override
            public void onFailure(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void loadStudentStatusData() {
        dashboardRepository.fetchStudentStatusData(new DashboardCallback.StudentStatusDataCallback() {
            @Override
            public void onSuccess(List<StudentStatusData.Status> data) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> updateStudentStatusChart(data));
            }

            @Override
            public void onFailure(String errorMessage) {
                showError(errorMessage);
            }
        });
    }

    private void updateRegistrationChart(List<RegistrationData.DailyRegistration> data) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            RegistrationData.DailyRegistration reg = data.get(i);
            entries.add(new Entry(i, reg.getCount()));
            labels.add(reg.getDay());
        }

        LineDataSet dataSet = new LineDataSet(entries, "Weekly Registrations");
        dataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setLineWidth(2f);
        dataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_dark));
        dataSet.setCircleRadius(4f);
        dataSet.setDrawCircleHole(false);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(android.R.color.holo_blue_light));
        registrationChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        registrationChart.setData(new LineData(dataSet));
    }

    private void updateStudentStatusChart(List<StudentStatusData.Status> data) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (StudentStatusData.Status status : data) {
            entries.add(new PieEntry(status.getPercentage(), status.getStatus()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);
        studentStatusChart.setData(new PieData(dataSet));
        studentStatusChart.invalidate();
    }

    private void showError(String message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() ->
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show()
            );
        }
    }
}