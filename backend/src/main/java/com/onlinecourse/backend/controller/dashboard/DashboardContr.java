package com.onlinecourse.backend.controller.dashboard;

import com.onlinecourse.backend.dto.dashboard.overview.DailyRegistrationResponse;
import com.onlinecourse.backend.dto.dashboard.overview.DashboardStatsResponse;
import com.onlinecourse.backend.dto.dashboard.overview.StudentStatusResponse;
import com.onlinecourse.backend.service.dashboard.overview.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardContr {
    private final DashboardService dashboardService;

    public DashboardContr(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardStatsResponse getDashboardStats() {
        return dashboardService.getDashboardStats();
    }

    @GetMapping("/registrations")
    public List<DailyRegistrationResponse> getRegistrationData() {
        return dashboardService.getRegistrationData();
    }

    @GetMapping("/student-status")
    public List<StudentStatusResponse> getStudentStatus() {
        return dashboardService.getStudentStatus();
    }
}
