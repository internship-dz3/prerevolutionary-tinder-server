package com.liga.internship.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liga.internship.server.domain.Gender;
import com.liga.internship.server.domain.dto.UserTo;
import com.liga.internship.server.domain.dto.UsersIdTo;
import com.liga.internship.server.service.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("POST dislike /api/v1/user/dislike")
    void testPostDislikeShouldReturnTrue() throws Exception {
        UsersIdTo usersIdTo = new UsersIdTo(1L, 2L);
        given(userService.dislike(usersIdTo.getCurrentUserId(), usersIdTo.getProcessUserId())).willReturn(true);

        mockMvc.perform(post("/api/v1/user/dislike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usersIdTo)))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/v1/user/3/admirers", "/api/v1/user/3/haters", "/api/v1/user/3/lovers",
            "/api/v1/user/3/tinder", "/api/v1/user/3/likes", "/api/v1/user/3/dislikes"})
    @DisplayName("GET Lists /api/v1/user/{id}/*typeOfList*")
    void testGetAdmirersHatersLikesDislikesLists(String uri) throws Exception {
        UserTo userTo1 = UserTo.builder()
                .telegramId(1L)
                .username("Вася")
                .description("Классный пацан")
                .gender(Gender.MALE)
                .look(Gender.ALL)
                .build();
        UserTo userTo2 = UserTo.builder()
                .telegramId(2L)
                .username("Люся")
                .description("Ну такое")
                .gender(Gender.FEMALE)
                .look(Gender.ALL)
                .build();
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getDislikeList(3L);
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getAdmirerList(3L);
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getLoveList(3L);
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getNotRatedList(3L);
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getFavoriteList(3L);
        doReturn(Lists.newArrayList(userTo1, userTo2)).when(userService).getHatersList(3L);

        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].telegramId", is(1)))
                .andExpect(jsonPath("$[1].telegramId", is(2)))
                .andExpect(jsonPath("$[0].username", is("Вася")))
                .andExpect(jsonPath("$[1].username", is("Люся")));
    }

    @Test
    @DisplayName("GET userTo by telegramId /api/v1/user/{id}")
    void testGetUserToByTelegramIdShouldBeOk() throws Exception {
        UserTo userTo = UserTo.builder()
                .telegramId(1L)
                .username("Игорян")
                .description("Классный пацан")
                .gender(Gender.MALE)
                .look(Gender.FEMALE)
                .build();
        given(userService.findById(1L)).willReturn(Optional.of(userTo));

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.telegramId").value(userTo.getTelegramId()))
                .andExpect(jsonPath("$.username", is(userTo.getUsername())));
    }

    @Test
    void testGetUserToByTelegramIdShouldBeNotFound() throws Exception {
        given(userService.findById(1L)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("correct POST /api/v1/user/like")
    void testPostLikeShouldReturnTrue() throws Exception {
        UsersIdTo usersIdTo = new UsersIdTo(1L, 2L);
        given(userService.like(usersIdTo.getCurrentUserId(), usersIdTo.getProcessUserId())).willReturn(true);

        mockMvc.perform(post("/api/v1/user/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(usersIdTo)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST login /api/v1/user/login")
    void testLoginIfUserExistShouldReturnUserTo() throws Exception {
        UserTo userTo = UserTo.builder()
                .telegramId(1L)
                .username("Вася")
                .description("Классный пацан")
                .gender(Gender.MALE)
                .look(Gender.ALL)
                .build();
        given(userService.findById(1)).willReturn(Optional.of(userTo));

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telegramId").value(userTo.getTelegramId()))
                .andExpect(jsonPath("$.username", is(userTo.getUsername())));
    }

    @Test
    @DisplayName("POST login User not exist /api/v1/user/login")
    void testLoginWhenUserNotExistShouldReturnNotAcceptable() throws Exception {
        given(userService.findById(1)).willReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(1L)))
                .andExpect(status().isNotFound());
    }


    @Test
    void register() {

    }

    @Test
    void update() {
    }
}