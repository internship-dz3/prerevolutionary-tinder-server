package com.liga.internship.server.controller;

import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.dto.UsersIdTo;
import com.liga.internship.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.liga.internship.server.common.MappingConstant.API_V_1_USER;

/**
 * Рест контроллер тиндер сервиса
 */
@Slf4j
@RestController
@RequestMapping(API_V_1_USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Запрос дизлайка
     *
     * @param usersIdTo - id пользователей
     * @return true при успешном выполнении операции
     */
    @PostMapping("/dislike")
    public Boolean dislike(@RequestBody UsersIdTo usersIdTo) {
        return userService.dislike(usersIdTo.getCurrentUserId(), usersIdTo.getProcessUserId());
    }

    /**
     * Запрос фанатов активного пользователя
     *
     * @param id - telegram id активного пользователя
     * @return список фанатов
     */
    @GetMapping("/{id}/admirers")
    public List<UserTo> getAdmirerList(@PathVariable Long id) {
        return userService.getAdmirerList(id);
    }

    /**
     * запрос данных активного пользователя
     *
     * @param id - telegram id активного пользователя
     * @return ResponseEntity с данными активного пользователя или NOT_FOUND если пользователь с данным id ненайден
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserTo> getById(@PathVariable Long id) {
        Optional<UserTo> userById = userService.findById(id);
        return userById.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("User with Id: %d not found", id), HttpStatus.NOT_FOUND));
    }

    /**
     * Запрос не любимцев активного пользователя
     *
     * @param id - telegram id активного пользователя
     * @return список Не любимцев
     */
    @GetMapping("/{id}/dislikes")
    public List<UserTo> getDislikesList(@PathVariable Long id) {
        return userService.getDislikeList(id);
    }

    /**
     * Запрос любимцев активного пользователя
     *
     * @param id - telegram id активного пользователя
     * @return список  любимцев
     */
    @GetMapping("/{id}/likes")
    public List<UserTo> getFavoritesList(@PathVariable Long id) {
        return userService.getFavoriteList(id);
    }

    /**
     * Запрос списка хейтеров
     *
     * @param id - telegram id активного пользователя
     * @return список хейтеров пользователя
     */
    @GetMapping("/{id}/haters")
    public List<UserTo> getHatersList(@PathVariable Long id) {
        return userService.getHatersList(id);
    }

    /**
     * Запрос списка Взаимности
     *
     * @param id - telegram id активного пользователя
     * @return Список пользователей со взаимностью
     */
    @GetMapping("/{id}/lovers")
    public List<UserTo> getLoveList(@PathVariable Long id) {
        return userService.getLoveList(id);
    }

    /**
     * Спиок для голосования
     *
     * @param id - телеграм id активного пользователя
     * @return список для голосования
     */
    @GetMapping("/{id}/tinder")
    public List<UserTo> getNotRatedList(@PathVariable Long id) {
        return userService.getNotRatedList(id);

    }

    /**
     * Запрос лайка
     *
     * @param usersIdTo - id пользователей
     * @return true при успешном выполнении операции
     */
    @PostMapping("/like")
    public boolean like(@RequestBody UsersIdTo usersIdTo) {
        return userService.like(usersIdTo.getCurrentUserId(), usersIdTo.getProcessUserId());
    }

    /**
     * Запрос на логин
     *
     * @param id - телеграм id активного пользователя
     * @return ResponseEntity с данными пользователя при успешном логине или NOT_FOUND
     */
    @PostMapping("/login")
    public ResponseEntity<UserTo> login(@RequestBody Long id) {
        Optional<UserTo> byTelegramId = userService.findById(id);
        return byTelegramId.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("User with Id: %d not found", id), HttpStatus.NOT_FOUND));
    }

    /**
     * Запрос регистрации
     *
     * @param userTo - данные нового пользователя
     * @return ResponseEntity с данными пользователя при успешной регистрации, или ответ NOT_ACCEPTABLE
     */
    @PostMapping("/register")
    public ResponseEntity<UserTo> register(@RequestBody UserTo userTo) {
        Optional<UserTo> registaredUser = userService.register(userTo);
        return registaredUser.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity("User already exist", HttpStatus.NOT_ACCEPTABLE));
    }

    /**
     * Запрос обновления пользователя
     *
     * @param userTo - данные обновляемого пользователя
     * @return true - при успешном обновлении данных пользователя
     */

    @PutMapping("/update")
    public boolean update(@RequestBody UserTo userTo) {
        return userService.update(userTo);
    }
}
