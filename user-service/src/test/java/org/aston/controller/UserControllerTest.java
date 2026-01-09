package org.aston.controller;

import org.aston.dto.CreateUserRequest;
import org.aston.dto.UpdateUserRequest;
import org.aston.dto.UserDto;
import org.aston.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    private UserDto sampleUser(Long id) {
        return UserDto.builder()
                .id(id)
                .name("Pal Palich")
                .email("pal@palich.com")
                .age(30)
                .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();
    }

    @Test
    void getAll_shouldReturnList() throws Exception {
        Mockito.when(service.getAll()).thenReturn(
                List.of(
                        sampleUser(1L),
                        UserDto.builder()
                                .id(2L)
                                .name("Alice")
                                .email("alice@example.com")
                                .age(25)
                                .createdAt(LocalDateTime.of(2025, 1, 2, 10, 0))
                                .build()
                )
        );

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(1))
                .andExpect(jsonPath("$._embedded.userDtoList[0].email").value("pal@palich.com"))
                .andExpect(jsonPath("$._embedded.userDtoList[1].name").value("Alice"))
                .andExpect(jsonPath("$._embedded.userDtoList[1].age").value(25));
    }

    @Test
    void getById_shouldReturnUser_whenFound() throws Exception {
        Mockito.when(service.getById(1L))
                .thenReturn(sampleUser(1L));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("pal@palich.com"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void getById_shouldReturn404_whenNotFound() throws Exception {
        Mockito.when(service.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_shouldReturnCreatedAndLocation() throws Exception {
        UserDto createdUser = sampleUser(10L);

        Mockito.when(service.create(any(CreateUserRequest.class)))
                .thenReturn(createdUser);

        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "name": "Pal Palich",
                                  "email": "pal@palich.com",
                                  "age": 30
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/10"))
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.email").value("pal@palich.com"));
    }

    @Test
    void update_shouldReturnUpdatedUser() throws Exception {
        UserDto updated = UserDto.builder()
                .id(1L)
                .name("Updated")
                .email("updated@example.com")
                .age(35)
                .createdAt(LocalDateTime.of(2025, 1, 1, 12, 0))
                .build();

        Mockito.when(service.update(eq(1L), any(UpdateUserRequest.class)))
                .thenReturn(updated);

        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "name": "Updated",
                                  "email": "updated@example.com",
                                  "age": 35
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.age").value(35));
    }

    @Test
    void delete_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(service).delete(1L);
    }

    //
    // Fail
    //

    @Test
    void create_shouldReturn400_whenNameTooShort() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Jo",
                              "email": "pal@palich.com",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenEmailBlank() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenEmailInvalid() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "not-an-email",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenAgeTooYoung() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "pal@palich.com",
                              "age": 10
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400_whenAgeTooOld() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "pal@palich.com",
                              "age": 150
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn400_whenNameTooShort() throws Exception {
        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Jo",
                              "email": "pal@palich.com",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn400_whenEmailBlank() throws Exception {
        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn400_whenInvalidEmail() throws Exception {
        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "wrong-email",
                              "age": 20
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn400_whenAgeTooYoung() throws Exception {
        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "pal@palich.com",
                              "age": 5
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_shouldReturn400_whenAgeTooOld() throws Exception {
        mockMvc.perform(
                        put("/users/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "name": "Pal Palich",
                              "email": "pal@palich.com",
                              "age": 200
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }
}
