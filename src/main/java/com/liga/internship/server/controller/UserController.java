package com.liga.internship.server.controller;

import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.liga.internship.server.common.MappingConstant.USER;

@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserTo>> getUserPageList() {
        return  ResponseEntity.ok(userService.findAll());
    }
}
