package com.kltn.service.impl;

import com.kltn.dto.response.dashboard.DashboardRevenueStatDTO;
import com.kltn.dto.response.dashboard.DashboardSummaryDTO;
import com.kltn.dto.response.dashboard.DashboardUserPostStatDTO;
import com.kltn.model.Post;
import com.kltn.model.User;
import com.kltn.repository.UserRepository;
import com.kltn.repository.PostRepository;
import com.kltn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.AbstractMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardServiceImp implements DashboardService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // Lấy thống kê doanh thu theo khoảng thời gian (chức năng đã bị xóa)
    @Override
    public List<DashboardRevenueStatDTO> getRevenueStatistics(String start, String end, String groupBy) {
        // Payment functionality removed
        return new ArrayList<>();
    }

    // Lấy tổng quan thống kê dashboard (tổng số người dùng và bài viết)
    @Override
    public DashboardSummaryDTO getDashboardSummary() {
        long totalUsers = userRepository.count();
        long totalPosts = postRepository.count();

        return new DashboardSummaryDTO(totalUsers, totalPosts);
    }

    // Lấy thống kê số lượng bài viết theo thời gian (ngày/tháng/năm)
    @Override
    public List<DashboardUserPostStatDTO> getUserPostStatistics(String start, String end, String groupBy) {
        // Specification cho Post
        Specification<Post> postSpec = (root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (start != null && !start.isEmpty()) {
                // Convert String to LocalDateTime for start date
                LocalDateTime startDate = LocalDate.parse(start)
                        .atStartOfDay();
                predicates.add(cb.greaterThanOrEqualTo(root.get("createAt"), startDate));
            }

            if (end != null && !end.isEmpty()) {
                // Convert String to LocalDateTime for end date (end of day)
                LocalDateTime endDate = LocalDate.parse(end)
                        .atTime(LocalTime.MAX);
                predicates.add(cb.lessThanOrEqualTo(root.get("createAt"), endDate));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };

        List<Post> posts = postRepository.findAll(postSpec);

        // Nhóm posts theo thời gian
        Map<String, Long> postGroups = posts.stream()
                .collect(Collectors.groupingBy(post -> {
                    LocalDateTime ldt = post.getCreateAt();
                    switch (groupBy.toLowerCase()) {
                        case "month":
                            return String.format("%d-%02d", ldt.getYear(), ldt.getMonthValue());
                        case "year":
                            return String.valueOf(ldt.getYear());
                        case "day":
                        default:
                            return ldt.toLocalDate().toString();
                    }
                }, Collectors.counting()));

        // Chuyển đổi kết quả
        List<DashboardUserPostStatDTO> result = postGroups.entrySet().stream()
                .map(entry -> new DashboardUserPostStatDTO(
                        entry.getKey(),
                        entry.getValue()))
                .sorted(Comparator.comparing(DashboardUserPostStatDTO::getGroupKey))
                .collect(Collectors.toList());

        return result;
    }
}
