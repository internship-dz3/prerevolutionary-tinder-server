package com.liga.internship.server.controller;

import com.liga.internship.server.common.MappingConstant;
import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.dto.UsersIdTo;
import com.liga.internship.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(MappingConstant.USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/dislike")
    public ResponseEntity<Boolean> dislikeAdmirer(@RequestBody UsersIdTo usersIdTo) {
        return ResponseEntity.ok(userService.dislikeAdmirer(usersIdTo.getCurrentUserId(), usersIdTo.getFavoriteUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserTo> getById(@PathVariable Long id) {
        Optional<UserTo> userById = userService.findUserById(id);
        return userById.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity(String.format("User with Id: %d not found", id), HttpStatus.NOT_FOUND));
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserTo>> getPageList() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/like")
    public ResponseEntity<Boolean> likeFavorite(@RequestBody UsersIdTo usersIdTo) {
        return ResponseEntity.ok(userService.likeFavorite(usersIdTo.getCurrentUserId(), usersIdTo.getFavoriteUserId()));
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserTo>> getFavoritesList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFavoriteList(id));
    }

    @GetMapping("/{id}/dislikes")
    public ResponseEntity<List<UserTo>> getAdmirersList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getAdmirersList(id));
    }

    @GetMapping("/{id}/haters")
    public ResponseEntity<List<UserTo>> getHatersList(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getHatersList(id));
    }
}
