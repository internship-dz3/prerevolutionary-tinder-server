package com.liga.internship.server.service;

import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.entity.UserEntity;
import com.liga.internship.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public boolean dislikeAdmirer(Long currentUserId, Long admirer) {
        Optional<UserEntity> current = repository.findById(currentUserId);
        Optional<UserEntity> favorite = repository.findById(admirer);
        if (current.isPresent() && favorite.isPresent()) {
            UserEntity curEntity = current.get();
            UserEntity admEntity = favorite.get();
            curEntity.getFavorites().remove(admEntity);
            curEntity.getAdmirers().add(admEntity);
            repository.save(curEntity);
            return true;
        }
        return false;
    }

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
                .gender(entity.getGender().name())
                .build();
    }

    public Optional<UserTo> findUserById(Long id) {
        Optional<UserEntity> userEntityById = repository.findById(id);
        return userEntityById.map(this::getUserToFromEntity);
    }

    public List<UserTo> getAdmirersList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> admirers = userEntity.getAdmirers();
        return getUserToList(admirers);
    }

    private List<UserTo> getUserToList(Set<UserEntity> userEntitySet) {
        List<UserTo> userToList = new ArrayList<>();
        for (UserEntity userEntity : userEntitySet) {
            userToList.add(getUserToFromEntity(userEntity));
        }
        return userToList;
    }

    public List<UserTo> getFavoriteList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> favorites = userEntity.getFavorites();
        return getUserToList(favorites);
    }

    public List<UserTo> getHatersList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> haters = userEntity.getHaters();
        return getUserToList(haters);
    }

    @Transactional
    public boolean likeFavorite(Long currentUserId, Long favoriteUserId) {
        Optional<UserEntity> current = repository.findById(currentUserId);
        Optional<UserEntity> favorite = repository.findById(favoriteUserId);
        if (current.isPresent() && favorite.isPresent()) {
            UserEntity curEntity = current.get();
            UserEntity favEntity = favorite.get();
            curEntity.getDislikes().remove(favEntity);
            curEntity.getFavorites().add(favEntity);
            repository.save(curEntity);
            return true;
        }
        return false;
    }

    private List<Long> getUserIdList(Set<UserEntity> entityList) {
        return entityList.stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());
    }
}
