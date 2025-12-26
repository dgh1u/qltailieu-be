package com.kltn.repository;

import com.kltn.model.Post;
import com.kltn.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByUser(User user, Pageable pageable);

    Optional<Post> findPostById(Long id);

    Page<Post> findAllByApprovedAndNotApprovedAndDel(boolean approved, boolean notApproved, boolean del,
            Pageable pageable);

    Page<Post> findAllByApprovedAndNotApproved(boolean approved, boolean notApproved, Pageable pageable);

    Page<Post> findAllByUser_EmailAndDelAndApproved(String email, boolean del, boolean approved, Pageable page);

    // Phương thức truy vấn các tài liệu chưa duyệt
    Page<Post> findByApprovedFalseAndNotApprovedFalse(Pageable pageable);
    // Phương thức tìm tài liệu nhà nguyên căn với các điều kiện lọc
}
