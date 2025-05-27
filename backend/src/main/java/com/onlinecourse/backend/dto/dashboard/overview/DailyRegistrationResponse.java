package com.onlinecourse.backend.dto.dashboard.overview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyRegistrationResponse {
    private String day;
    private int count;
}