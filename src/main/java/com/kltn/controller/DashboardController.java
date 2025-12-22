package com.kltn.controller;

import com.kltn.dto.response.dashboard.DashboardRevenueStatDTO;
import com.kltn.dto.response.dashboard.DashboardSummaryDTO;
import com.kltn.dto.response.dashboard.DashboardUserPostStatDTO;
import com.kltn.service.DashboardService;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // API thống kê doanh thu theo thời gian
    @ApiOperation(value = "Thống kê doanh thu và số giao dịch theo ngày/tháng/năm")
    @GetMapping("/revenue")
    public ResponseEntity<List<DashboardRevenueStatDTO>> getRevenueStatistics(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "day") @Pattern(regexp = "day|month|year", message = "groupBy phải là day, month hoặc year") String groupBy) {
        List<DashboardRevenueStatDTO> stats = dashboardService.getRevenueStatistics(start, end, groupBy);
        return ResponseEntity.ok(stats);
    }

    // API lấy tổng quan thống kê dashboard
    @ApiOperation(value = "Thống kê tổng hợp số liệu trên dashboard")
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryDTO> getDashboardSummary() {
        DashboardSummaryDTO summary = dashboardService.getDashboardSummary();
        return ResponseEntity.ok(summary);
    }

    // API thống kê số lượng bài viết theo thời gian
    @ApiOperation(value = "Thống kê số liệu bài viết của người dùng theo ngày/tháng")
    @GetMapping("/post-stats")
    public ResponseEntity<List<DashboardUserPostStatDTO>> getUserPostStatistics(
            @RequestParam(required = false) String start,
            @RequestParam(required = false) String end,
            @RequestParam(defaultValue = "month") String groupBy) {
        return ResponseEntity.ok(dashboardService.getUserPostStatistics(start, end, groupBy));
    }
}
