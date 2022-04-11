package com.liga.internship.server.service;

import com.liga.internship.server.domain.Gender;
import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.entity.UserEntity;
import com.liga.internship.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.liga.internship.server.domain.Gender.ALL;

/**
 * Сервис для UserConroller
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    /**
     * Дизлайк
     *
     * @param activeUserId  - телеграм id активного пользователя
     * @param admirerUserId - телеграм id не понравившегося пользователя
     * @return true если оба пользователя присутствуют в базе
     */
    @Transactional
    public boolean dislike(long activeUserId, long admirerUserId) {
        if (activeUserId == admirerUserId) {
            return false;
        }
        Optional<UserEntity> current = repository.findUserEntityByTelegramId(activeUserId);
        Optional<UserEntity> admirer = repository.findUserEntityByTelegramId(admirerUserId);
        if (current.isPresent() && admirer.isPresent()) {
            UserEntity curEntity = current.get();
            UserEntity admEntity = admirer.get();
            curEntity.getFavorites().remove(admEntity);
            curEntity.getDislikes().add(admEntity);
            repository.save(curEntity);
            log.info("active userID {} dislike userID {}", activeUserId, admirerUserId);
            return true;
        }
        log.info("active userID {} wrong dislike userID {}", activeUserId, admirerUserId);
        return false;
    }

    /**
     * Поиск пользователя по телеграм id
     *
     * @param telegramId - телеграм id пользователя
     * @return опциональный UserTo
     */
    public Optional<UserTo> findById(long telegramId) {
        log.info("findById telegramID: {}", telegramId);
        Optional<UserEntity> userEntityById = repository.findUserEntityByTelegramId(telegramId);
        return userEntityById.map(this::getUserToFromEntity);
    }

    public List<UserTo> getAdmirerList(long activeUserId) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            admirers.removeAll(favorites);
            return getUserToList(admirers);
        }
        return new ArrayList<>();
    }

    /**
     * Список не любимцев
     *
     * @param activeUserId - телеграм activeUserId пользователя
     * @return список не любицев
     */
    public List<UserTo> getDislikeList(long activeUserId) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> dislikes = userEntity.getDislikes();
            log.info("getDislikeList for userID {}, list size {}", activeUserId, dislikes.size());
            return getUserToList(dislikes);
        }
        return new ArrayList<>();
    }

    /**
     * Список любимцев без взаимности
     *
     * @param activeUserId - телеграм activeUserId пользователя
     * @return список любицев
     */
    public List<UserTo> getFavoriteList(long activeUserId) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            favorites.removeAll(admirers);
            log.info("getFavoriteList for userID {}, list size {}", activeUserId, favorites.size());
            return getUserToList(favorites);
        }
        return new ArrayList<>();
    }

    /**
     * Список хейтеров
     *
     * @param activeUserId - телеграм activeUserId пользователя
     * @return список хейтеров
     */
    public List<UserTo> getHatersList(long activeUserId) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> haters = userEntity.getHaters();
            log.info("getHatersList for userID {}, list size {}", activeUserId, haters.size());
            return getUserToList(haters);
        }
        return new ArrayList<>();
    }

    /**
     * Список взаимности
     *
     * @param activeUserId - телеграм activeUserId пользователя
     * @return список взаимных любицев
     */
    public List<UserTo> getLoveList(long activeUserId) {
        Optional<UserEntity> optionalUser = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            Set<UserEntity> favorites = userEntity.getFavorites();
            Set<UserEntity> admirers = userEntity.getAdmirers();
            List<UserTo> loveList = getLoveList(favorites, admirers);
            log.info("getLoveList for userID {} size: {}", activeUserId, loveList.size());
            return loveList;
        }
        return new ArrayList<>();
    }

    // Переделать на запрос в репозиторий
    public List<UserTo> getNotRatedList(long activeUserId) {
        Optional<UserEntity> optionalUserEntity = repository.findUserEntityByTelegramId(activeUserId);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            List<UserEntity> notRatedEntitiesByLook;
            Gender look = userEntity.getLook();
            if (look == ALL) {
                notRatedEntitiesByLook = repository.findAll();
            } else {
                notRatedEntitiesByLook = repository.findAllByGender(look);
            }
            notRatedEntitiesByLook.remove(userEntity);
            log.info("getNotRatedList for userID {} size: {}", activeUserId, notRatedEntitiesByLook.size());
            Collections.shuffle(notRatedEntitiesByLook);
            return notRatedEntitiesByLook.stream()
                    .map(this::getUserToFromEntity)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Лайк
     *
     * @param activeUserId  - телеграм id активного пользователя
     * @param admirerUserId - телеграм id не понравившегося пользователя
     * @return true если оба пользователя присутствуют в базе
     */
    @Transactional
    public boolean like(long activeUserId, long admirerUserId) {
        if (activeUserId == admirerUserId) {
            return false;
        }
        Optional<UserEntity> current = repository.findUserEntityByTelegramId(activeUserId);
        Optional<UserEntity> favorite = repository.findUserEntityByTelegramId(admirerUserId);
        if (current.isPresent() && favorite.isPresent()) {
            UserEntity curEntity = current.get();
            UserEntity favEntity = favorite.get();
            curEntity.getDislikes().remove(favEntity);
            curEntity.getFavorites().add(favEntity);
            repository.save(curEntity);
            log.info("active userID {} like userID {}", activeUserId, admirerUserId);
            return curEntity.getAdmirers().contains(favEntity);
        }
        log.info("active userID {} wrong like userID {}", activeUserId, admirerUserId);
        return false;
    }

    /**
     * Регистрация нового пользователя
     *
     * @param userTo - данные регистрируемого пользователя
     * @return Опциональный UserTo при успешной регистрации
     */
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
            log.info("register new user: {}", userTo);
            return Optional.of(getUserToFromEntity(newUser));
        }
    }

    /**
     * Обновление существующего пользователя
     *
     * @param userTo - данные обновляемого пользователя
     * @return true если обновлен только 1 пользователь
     */
    @Transactional
    public boolean update(UserTo userTo) {
        int numberOfUpdatedUsers = repository.updateUser(
                userTo.getTelegramId(),
                userTo.getUsername(),
                userTo.getDescription(),
                userTo.getGender(),
                userTo.getLook()
        );
        log.info("update user: {}", userTo);
        return numberOfUpdatedUsers == 1;
    }

    private List<UserTo> getLoveList(Set<UserEntity> favorites, Set<UserEntity> admirers) {
        return favorites.stream()
                .filter(admirers::contains)
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }

    private UserEntity getUserEnityFromUserTo(UserTo userTo) {
        return UserEntity.builder()
                .telegramId(userTo.getTelegramId())
                .username(userTo.getUsername())
                .gender(userTo.getGender())
                .description(userTo.getDescription())
                .look(userTo.getLook())
                .build();
    }

    private UserTo getUserToFromEntity(UserEntity entity) {
        return UserTo.builder()
                .telegramId(entity.getTelegramId())
                .username(entity.getUsername())
                .description(entity.getDescription())
                .gender(entity.getGender())
                .look(entity.getLook())
                .build();
    }

    private List<UserTo> getUserToList(Set<UserEntity> favorites) {
        return favorites.stream()
                .map(this::getUserToFromEntity)
                .collect(Collectors.toList());
    }
}
