package com.liga.internship.server.service;

import com.liga.internship.server.domain.Gender;
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

import static com.liga.internship.server.domain.Gender.ALL;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    @Transactional
    public boolean dislikeAdmirer(Long currentUserId, Long admirer) {
        Optional<UserEntity> current = repository.findUserEntityByTelegramId(currentUserId);
        Optional<UserEntity> favorite = repository.findUserEntityByTelegramId(admirer);
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

    public List<UserTo> findAll() {
        return repository.findAll().stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    private UserTo getUserToFromEntity(UserEntity entity) {
        return UserTo.builder()
                .telegramId(entity.getTelegramId())
                .username(entity.getUsername())
                .age(entity.getAge())
                .description(entity.getDescription())
                .gender(entity.getGender())
                .look(entity.getLook())
                .build();
    }

    public Optional<UserTo> findById(Long id) {
        Optional<UserEntity> userEntityById = repository.findUserEntityByTelegramId(id);
        return userEntityById.map(this::getUserToFromEntity);
    }

    public Optional<UserTo> findByTelegramId(Long telegramId) {
        Optional<UserEntity> userEntityByTelegramId = repository.findUserEntityByTelegramId(telegramId);
        return userEntityByTelegramId.map(this::getUserToFromEntity);

    }

    // Переделать на запрос в репозиторий
    public List<UserTo> findNotRatedUsers(Long id) {
        Optional<UserEntity> optionalUserEntity = repository.findUserEntityByTelegramId(id);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            List<UserEntity> notRatedEntitiesByLook;
            Gender look = userEntity.getLook();
            if (look == ALL) {
                notRatedEntitiesByLook = repository.findAll();
            } else {
                notRatedEntitiesByLook = repository.findAllByGender(look);
            }
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> dislikes = userEntity.getDislikes();
            notRatedEntitiesByLook.remove(userEntity);
            notRatedEntitiesByLook.removeAll(favorites);
            notRatedEntitiesByLook.removeAll(dislikes);
            return notRatedEntitiesByLook.stream()
                    .map(this::getUserToFromEntity)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<UserTo> getAdmirerList(Long id) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(id);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            admirers.removeAll(favorites);
            return getUserToList(admirers);
        }
        return new ArrayList<>();
    }

    private List<UserTo> getUserToList(Set<UserEntity> favorites) {
        return favorites.stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    public List<UserTo> getDislikes(Long id) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(id);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> dislikes = userEntity.getDislikes();
            return getUserToList(dislikes);
        }
        return new ArrayList<>();
    }

    public List<UserTo> getFavoriteList(Long id) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(id);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            favorites.removeAll(admirers);
            return getUserToList(favorites);
        }
        return new ArrayList<>();
    }

    public List<UserTo> getHatersList(Long id) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(id);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> haters = userEntity.getHaters();
            return getUserToList(haters);
        }
        return new ArrayList<>();
    }

    public List<UserTo> getLoveList(Long id) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(id);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            return getLoveList(favorites, admirers);
        }
        return new ArrayList<>();
    }

    private List<UserTo> getLoveList(Set<UserEntity> favorites, Set<UserEntity> admirers) {
        return favorites.stream()
                .filter(admirers::contains)
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean likeFavorite(Long currentUserId, Long favoriteUserId) {
        Optional<UserEntity> current = repository.findUserEntityByTelegramId(currentUserId);
        Optional<UserEntity> favorite = repository.findUserEntityByTelegramId(favoriteUserId);
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
            update(userTo);
            return Optional.of(userTo);
        } else {
            UserEntity userEntity = getUserEnityFromUserTo(userTo);
            UserEntity newUser = repository.save(userEntity);
            return Optional.of(getUserToFromEntity(newUser));
        }
    }

    private UserEntity getUserEnityFromUserTo(UserTo userTo) {
        return UserEntity.builder()
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
                userTo.getTelegramId(),
                userTo.getUsername(),
                userTo.getAge(),
                userTo.getDescription(),
                userTo.getGender(),
                userTo.getLook()
        );
        return i == 1;
    }
}
