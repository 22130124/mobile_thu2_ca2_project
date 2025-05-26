package com.example.onlinecoursesapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Default to 10.0.2.2 (localhost equivalent) for development in emulator
    private static String BASE_URL = "http://10.0.2.2:8080";
    private static Retrofit retrofit = null;

    public static void setBaseUrl(String url) {
        BASE_URL = url;
        // Reset retrofit instance to recreate with new URL
        retrofit = null;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CourseApiService getCourseApiService() {
        return getClient().create(CourseApiService.class);
    }

    public static UserAPIService getUserApiService() {
        return getClient().create(UserAPIService.class);
    }

    public static EnrollmentApiService getEnrollmentApiService() {
        return getClient().create(EnrollmentApiService.class);
    }

    public static LessonProgressApiService getLessonProgressApiService() {
        return getClient().create(LessonProgressApiService.class);
    }

    public static DashboardApiService getDashboardApiService(){
        return getClient().create(DashboardApiService.class);
    }
}
