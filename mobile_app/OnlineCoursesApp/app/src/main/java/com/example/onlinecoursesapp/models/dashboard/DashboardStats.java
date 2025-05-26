package com.example.onlinecoursesapp.models.dashboard;
import com.google.gson.annotations.SerializedName;
public class DashboardStats {
        @SerializedName("totalStudents")
        private int totalStudents;

        @SerializedName("totalCourses")
        private int totalCourses;

        @SerializedName("totalHours")
        private int totalHours;

        @SerializedName("completionRate")
        private double completionRate;

        @SerializedName("newToday")
        private int newToday;

        @SerializedName("newWeek")
        private int newWeek;

        // Getters
        public int getTotalStudents() { return totalStudents; }
        public int getTotalCourses() { return totalCourses; }
        public int getTotalHours() { return totalHours; }
        public double getCompletionRate() { return completionRate; }
        public int getNewToday() { return newToday; }
        public int getNewWeek() { return newWeek; }

}
