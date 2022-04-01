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
            curEntity.getDislikes().add(admEntity);
            repository.save(curEntity);
            return true;
        }
        return false;
    }

    public Optional<UserTo> findById(Long id) {
        Optional<UserEntity> userEntityById = repository.findById(id);
        return userEntityById.map(this::getUserToFromEntity);
    }

    private UserTo getUserToFromEntity(UserEntity entity) {
        return UserTo.builder()
                .id(entity.getId())
                .telegramId(entity.getTelegramId())
                .username(entity.getUsername())
                .age(entity.getAge())
                .description(entity.getDescription())
                .gender(entity.getGender())
                .look(entity.getLook())
                .build();
    }

    public Optional<UserTo> findByTelegramId(Long telegramId) {
        Optional<UserEntity> userEntityByTelegramId = repository.findUserEntityByTelegramId(telegramId);
        return userEntityByTelegramId.map(this::getUserToFromEntity);

    }

    public List<UserTo> findNotRatedUsers(Long id) {
        return findAll();
    }

    public List<UserTo> findAll() {
        return repository.findAll().stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    public List<UserTo> getAdmirerList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> favorites = userEntity.getFavorites();
        Set<UserEntity> admirers = userEntity.getAdmirers();
        admirers.removeAll(favorites);
        return getFavoriteList(favorites);
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
        Set<UserEntity> admirers = userEntity.getAdmirers();
        favorites.removeAll(admirers);
        return getFavoriteList(favorites);
    }

    private List<UserTo> getFavoriteList(Set<UserEntity> favorites) {
        return favorites.stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    public List<UserTo> getHatersList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> haters = userEntity.getHaters();
        return getUserToList(haters);
    }

    public List<UserTo> getLoveList(Long id) {
        UserEntity userEntity = repository.getById(id);
        Set<UserEntity> favorites = userEntity.getFavorites();
        Set<UserEntity> admirers = userEntity.getAdmirers();
        return getLoveList(favorites, admirers);
    }

    private List<UserTo> getLoveList(Set<UserEntity> favorites, Set<UserEntity> admirers) {
        return favorites.stream()
                .filter(admirers::contains)
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
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

    @Transactional
    public Optional<UserTo> register(UserTo userTo) {
        long telegramId = userTo.getTelegramId();
        Optional<UserEntity> userEntityByTelegramId = repository.findUserEntityByTelegramId(telegramId);
        if (userEntityByTelegramId.isPresent()) {
            return Optional.empty();
        } else {
            UserEntity userEntity = getUserEnityFromUserTo(userTo);
            UserEntity newUser = repository.save(userEntity);
            return Optional.of(getUserToFromEntity(newUser));
        }
    }

    private UserEntity getUserEnityFromUserTo(UserTo userTo) {
        return UserEntity.builder()
                .id(userTo.getId())
                .telegramId(userTo.getTelegramId())
                .username(userTo.getUsername())
                .age(userTo.getAge())
                .gender(userTo.getGender())
                .description(userTo.getDescription())
                .look(userTo.getLook())
                .build();
    }

    @Transactional
    public boolean update(UserTo userTo) {
        int i = repository.updateUser(
                userTo.getId(),
                userTo.getUsername(),
                userTo.getAge(),
                userTo.getDescription(),
                userTo.getGender(),
                userTo.getLook()
        );
        return i == 1;
    }
}
