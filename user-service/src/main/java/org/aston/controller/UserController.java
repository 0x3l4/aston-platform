package org.aston.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.aston.dto.*;
import org.aston.service.UserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "Users", description = "API управления пользователями")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(summary = "Получить список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей")
    @GetMapping
    public CollectionModel<EntityModel<UserDto>> getAll() {

        List<EntityModel<UserDto>> users = service.getAll().stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class)
                                .getById(user.getId())).withSelfRel()
                ))
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAll()).withSelfRel());
    }

    @Operation(summary = "Получить пользователя по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> getById(@PathVariable Long id) {

        UserDto user = service.getById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        EntityModel<UserDto> model = EntityModel.of(user,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("users"),
                linkTo(methodOn(UserController.class).update(id, null)).withRel("update"),
                linkTo(methodOn(UserController.class).delete(id)).withRel("delete")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Создать нового пользователя")
    @ApiResponse(responseCode = "201", description = "Пользователь создан")
    @PostMapping
    public ResponseEntity<EntityModel<UserDto>> create(
            @Valid @RequestBody CreateUserRequest request) {

        UserDto created = service.create(request);

        EntityModel<UserDto> model = EntityModel.of(created,
                linkTo(methodOn(UserController.class)
                        .getById(created.getId())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("users")
        );

        return ResponseEntity
                .created(URI.create("/users/" + created.getId()))
                .body(model);
    }

    @Operation(summary = "Обновить пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь обновлён")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        UserDto updated = service.update(id, request);

        EntityModel<UserDto> model = EntityModel.of(updated,
                linkTo(methodOn(UserController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).getAll()).withRel("users")
        );

        return ResponseEntity.ok(model);
    }

    @Operation(summary = "Удалить пользователя")
    @ApiResponse(responseCode = "204", description = "Пользователь удалён")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

