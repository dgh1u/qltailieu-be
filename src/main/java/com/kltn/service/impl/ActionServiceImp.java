package com.kltn.service.impl;

import com.kltn.exception.DataNotFoundException;
import com.kltn.model.Action;
import com.kltn.model.Post;
import com.kltn.model.User;
import com.kltn.model.enums.ActionName;
import com.kltn.repository.ActionRepository;
import com.kltn.repository.custom.CustomActionQuery;
import com.kltn.service.ActionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActionServiceImp implements ActionService {

    private final ActionRepository actionRepository;

    // Tạo hoạt động mới cho bài viết
    @Override
    public void createAction(Post post, User user, ActionName actionName) {
        try {
            Action action = new Action(post, user, actionName);
            actionRepository.save(action);
        } catch (Exception e) {
            log.error("Lỗi khi tạo hoạt động: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi tạo hoạt động: " + e.getMessage());
        }
    }

    // Lấy danh sách hoạt động theo bộ lọc và phân trang
    @Override
    public Page<Action> getAction(CustomActionQuery.ActionFilterParam param, PageRequest pageRequest) {
        try {
            Specification<Action> specification = CustomActionQuery.getFilterAction(param);
            return actionRepository.findAll(specification, pageRequest);
        } catch (Exception e) {
            throw new DataNotFoundException("Không có bài viết nào được tìm thấy! " + e.getMessage());
        }
    }

    // Đánh dấu hoạt động đã được đọc
    @Override
    public void markActionAsRead(Long actionId) {
        Optional<Action> actionOpt = actionRepository.findById(actionId);
        if (actionOpt.isPresent()) {
            Action action = actionOpt.get();
            action.setIsRead(true);
            actionRepository.save(action);
        } else {
            throw new DataNotFoundException("Không tìm thấy hoạt động với id: " + actionId);
        }
    }

}
