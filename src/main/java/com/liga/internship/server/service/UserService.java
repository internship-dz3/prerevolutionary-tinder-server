package com.liga.internship.server.service;

import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.entity.UserEntity;
import com.liga.internship.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<UserTo> findAll() {
        return repository.findAll().stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    private UserTo getUserToFromEntity(UserEntity entity) {
        return UserTo.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .age(entity.getAge())
                .description(entity.getDescription())
                .gender(entity.getGender())
                .build();
    }

    private List<Long> getUserIdList(Set<UserEntity> entityList) {
        return entityList.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());
    }
}
