package com.kltn.repository.custom;

import com.kltn.model.Criteria;
import com.kltn.model.Post;
import com.kltn.utils.CriteriaBuilderUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public class CustomCriteriaQuery {

    private CustomCriteriaQuery() {
    }

    @Data
    @NoArgsConstructor
    public static class CriteriaFilterParam {
        private String keywords;
        private String motel;
        // Thêm mới: danh sách các giá trị motel
        private List<String> motels;
        private String secondMotel;
        private String major;
    }

    public static Specification<Criteria> getFilterCriteria(CriteriaFilterParam param) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Criteria, Post> postJoin = root.join("post", JoinType.LEFT);

            // Lọc theo tiêu đề tài liệu
            if (param.getKeywords() != null) {
                predicates.add(CriteriaBuilderUtil.createPredicateForSearchInsensitive(
                        postJoin, criteriaBuilder, param.getKeywords(), "title"));
            }

            // Cập nhật xử lý motel - hỗ trợ cả hai phương thức
            if (param.getMotels() != null && !param.getMotels().isEmpty()) {
                // Sử dụng IN khi có nhiều giá trị
                predicates.add(root.get("motel").in(param.getMotels()));
            } else if (param.getMotel() != null) {
                // Vẫn giữ phương thức cũ để đảm bảo tương thích ngược
                predicates.add(criteriaBuilder.equal(root.get("motel"), param.getMotel()));
            }

            if (param.getSecondMotel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("secondMotel"), param.getSecondMotel()));
            }

            if (param.getMajor() != null) {
                predicates.add(criteriaBuilder.equal(root.get("major"), param.getMajor()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}