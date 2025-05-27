package com.example.onlinecoursesapp.models.dashboard;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RegistrationData {
    @SerializedName("dailyRegistrations")
    private List<DailyRegistration> dailyRegistrations;

    public List<DailyRegistration> getDailyRegistrations() {
        return dailyRegistrations;
    }
    public static class DailyRegistration {
        @SerializedName("day")
        private String day;

        @SerializedName("count")
        private int count;

        public String getDay() { return day; }
        public int getCount() { return count; }
    }
}
