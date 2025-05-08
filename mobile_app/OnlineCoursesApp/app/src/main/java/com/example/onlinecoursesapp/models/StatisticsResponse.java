package com.example.onlinecoursesapp.models;

import com.google.gson.annotations.SerializedName;

public class StatisticsResponse {

        @SerializedName("totalLearningTimeMinutes")
        private int totalLearningTimeMinutes;

        @SerializedName("totalCoursesEnrolled")
        private int totalCoursesEnrolled;

        @SerializedName("completedCourses")
        private int completedCourses;

        @SerializedName("inProgressCourses")
        private int inProgressCourses;

        @SerializedName("averageCompletionRate")
        private float averageCompletionRate;

        @SerializedName("totalAchievements")
        private int totalAchievements;

        // Getters and Setters
        public int getTotalLearningTimeMinutes() {
            return totalLearningTimeMinutes;
        }

        public void setTotalLearningTimeMinutes(int totalLearningTimeMinutes) {
            this.totalLearningTimeMinutes = totalLearningTimeMinutes;
        }

        public int getTotalCoursesEnrolled() {
            return totalCoursesEnrolled;
        }

        public void setTotalCoursesEnrolled(int totalCoursesEnrolled) {
            this.totalCoursesEnrolled = totalCoursesEnrolled;
        }

        public int getCompletedCourses() {
            return completedCourses;
        }

        public void setCompletedCourses(int completedCourses) {
            this.completedCourses = completedCourses;
        }

        public int getInProgressCourses() {
            return inProgressCourses;
        }

        public void setInProgressCourses(int inProgressCourses) {
            this.inProgressCourses = inProgressCourses;
        }

        public float getAverageCompletionRate() {
            return averageCompletionRate;
        }

        public void setAverageCompletionRate(float averageCompletionRate) {
            this.averageCompletionRate = averageCompletionRate;
        }

        public int getTotalAchievements() {
            return totalAchievements;
        }

        public void setTotalAchievements(int totalAchievements) {
            this.totalAchievements = totalAchievements;
        }
}
