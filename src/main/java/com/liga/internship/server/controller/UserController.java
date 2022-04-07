package com.liga.internship.server.controller;

import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.dto.UsersIdTo;
import com.liga.internship.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.liga.internship.server.common.MappingConstant.USER;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/dislike")
    public ResponseEntity<Boolean> dislikeAdmirer(@RequestBody UsersIdTo usersIdTo) {
        return ResponseEntity.ok(userService.dislikeAdmirer(usersIdTo.getCurrentUserId(), usersIdTo.getFavoriteUserId()));
    }

    @GetMapping("/{id}/admirers")
    public ResponseEntity<List<UserTo>> getAdmirerList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAdmirerList(id));
    }

    @GetMapping("/{id}/dislikes")
    public ResponseEntity<List<UserTo>> getAdmirersList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getDislikes(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTo> getById(@PathVariable Long id) {
        Optional<UserTo> userById = userService.findById(id);
        return userById.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("User with Id: %d not found", id), HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserTo>> getFavoritesList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFavoriteList(id));
    }

    @GetMapping("/{id}/haters")
    public ResponseEntity<List<UserTo>> getHatersList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getHatersList(id));
    }

    @GetMapping("/{id}/lovers")
    public ResponseEntity<List<UserTo>> getLoveList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getLoveList(id));
    }

    @PostMapping("/list")
    public ResponseEntity<List<UserTo>> votingList(@RequestBody Long id) {
        return ResponseEntity.ok(userService.findNotRatedUsers(id));
    }

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeFavorite(@RequestBody UsersIdTo usersIdTo) {
        return ResponseEntity.ok(userService.likeFavorite(usersIdTo.getCurrentUserId(), usersIdTo.getFavoriteUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<UserTo> login(@RequestBody Long telegramId) {
        Optional<UserTo> byTelegramId = userService.findByTelegramId(telegramId);
        return byTelegramId.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("User with Id: %d not found", telegramId), HttpStatus.NOT_FOUND));
    }

    @PostMapping("/register")
    public ResponseEntity<UserTo> register(@RequestBody UserTo userTo) {
        Optional<UserTo> registaredUser = userService.register(userTo);
        return registaredUser.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity("User already exist", HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody UserTo userTo) {
        return ResponseEntity.ok(userService.update(userTo));
    }
}
