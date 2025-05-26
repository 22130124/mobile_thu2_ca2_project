package com.example.onlinecoursesapp.utils;

public class FormatTime {
    public static String formatDuration(double durationMinutes) {
        int totalMinutes = (int) durationMinutes;
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        int seconds = (int) Math.round((durationMinutes - totalMinutes) * 60);

        if (hours > 0) {
            return hours + " giờ " + minutes + " phút " + seconds + " giây";
        } else if (minutes > 0) {
            return minutes + " phút " + seconds + " giây";
        } else {
            return seconds + " giây";
        }
    }

}
