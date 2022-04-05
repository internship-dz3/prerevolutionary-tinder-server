package com.liga.internship.server.repository;

import com.liga.internship.server.domain.Gender;
import com.liga.internship.server.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByGender(Gender look);

    Optional<UserEntity> findUserEntityByTelegramId(Long telegramId);

    @Modifying
    @Query("update UserEntity u set u.username = :username, " +
            "u.age = :age," +
            "u.description = :description," +
            "u.gender = :gender," +
            "u.look = :look " +
            "where u.telegramId = :telegram_id")
    int updateUser(@Param(value = "telegram_id") long telegramId,
                   @Param(value = "username") String username,
                   @Param(value = "age") int age,
                   @Param(value = "description") String description,
                   @Param(value = "gender") Gender gender,
                   @Param(value = "look") Gender look);
}
