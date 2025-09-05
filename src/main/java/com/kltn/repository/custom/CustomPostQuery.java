package com.kltn.repository.custom;

import com.kltn.constant.Constant;
import com.kltn.model.Criteria;
import com.kltn.model.Post;
import com.kltn.model.User;
import com.kltn.utils.CriteriaBuilderUtil;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomPostQuery {

    private CustomPostQuery() {}

    @Data
    @NoArgsConstructor
    public static class PostFilterParam extends CustomCriteriaQuery.CriteriaFilterParam {
        private String keywords;
        private Boolean approved;
        private Boolean notApproved;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String type;
        private Boolean del;
        private Long userId;
        private String sortField;
        private String sortType;
    }

    public static Specification<Post> getFilterPost(PostFilterParam param) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Lọc theo title của Post
            if (param.keywords != null) {
                predicates.add(CriteriaBuilderUtil.createPredicateForSearchInsensitive(root, criteriaBuilder,
                        param.keywords, "title"));
            }

            // Lọc theo trạng thái approved và notApproved
            if (param.getApproved() != null) {
                predicates.add(criteriaBuilder.equal(root.get("approved"), param.getApproved()));
            }
            if (param.getNotApproved() != null) {
                predicates.add(criteriaBuilder.equal(root.get("notApproved"), param.getNotApproved()));
            }

            // Lọc theo trạng thái hiển thị del
            if (param.getDel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("del"), param.getDel()));
            }

            // Lọc theo ngày tạo
            if (param.getStartDate() != null && param.getEndDate() != null) {
                predicates.add(criteriaBuilder.between(root.get("createAt"),
                        param.getStartDate(), param.getEndDate()));
            } else if (param.getStartDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createAt"), param.getStartDate()));
            } else if (param.getEndDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), param.getEndDate()));
            }

            // Lọc theo userId
            if (param.getUserId() != null) {
                Join<Post, User> userJoin = root.join("user");
                predicates.add(criteriaBuilder.equal(userJoin.get("id"), param.getUserId()));
            }

            // Nếu có bất kỳ trường lọc của Criteria nào được set, thực hiện join với Criteria
            if (param.getMotel() != null || param.getMotels() != null ||
                    param.getSecondMotel() != null || param.getMajor() != null ||
                    (param.getKeywords() != null && !param.getKeywords().isEmpty())
            ) {
                Join<Post, Criteria> criteriaJoin = root.join("criteria", JoinType.LEFT);

                // Cập nhật xử lý motel - hỗ trợ cả hai phương thức
                if (param.getMotels() != null && !param.getMotels().isEmpty()) {
                    // Sử dụng IN khi có nhiều giá trị
                    predicates.add(criteriaJoin.get("motel").in(param.getMotels()));
                } else if (param.getMotel() != null) {
                    // Vẫn giữ phương thức cũ để đảm bảo tương thích ngược
                    predicates.add(criteriaBuilder.equal(criteriaJoin.get("motel"), param.getMotel()));
                }

                if (param.getSecondMotel() != null) {
                    predicates.add(criteriaBuilder.equal(criteriaJoin.get("secondMotel"), param.getSecondMotel()));
                }

                // Lọc theo major
                if (param.getMajor() != null) {
                    predicates.add(criteriaBuilder.equal(criteriaJoin.get("major"), param.getMajor()));
                }
            }

            // Xử lý sắp xếp
            if (param.sortField != null && !param.sortField.equals("")) {
                // Sắp xếp theo các trường của Post
                if (param.sortType.equals(Constant.SortType.DESC) || param.sortType.equals("")) {
                    query.orderBy(criteriaBuilder.desc(root.get(param.sortField)));
                } else if (param.sortType.equals(Constant.SortType.ASC)) {
                    query.orderBy(criteriaBuilder.asc(root.get(param.sortField)));
                }
            } else {
                // Mặc định sắp xếp theo ID giảm dần (tin mới nhất trước)
                query.orderBy(criteriaBuilder.desc(root.get("id")));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}