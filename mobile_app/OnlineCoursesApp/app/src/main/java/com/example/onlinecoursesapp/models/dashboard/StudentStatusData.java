package com.example.onlinecoursesapp.models.dashboard;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StudentStatusData {
    @SerializedName("statuses")
    private List<Status> statuses;

    public List<Status> getStatuses() {
        return statuses;
    }
    public static class Status {
        @SerializedName("status")
        private String status;

        @SerializedName("percentage")
        private float percentage;

        public String getStatus() { return status; }
        public float getPercentage() { return percentage; }
    }
}
