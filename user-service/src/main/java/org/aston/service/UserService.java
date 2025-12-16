package org.aston.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aston.dto.*;
import org.aston.entity.User;
import org.aston.exception.UserNotFoundException;
import org.aston.mapper.UserMapper;
import org.aston.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDto> getAll() {
        List<User> users = repository.findAll();

        if (users.isEmpty()) {
            log.warn("Business: no users found");
        } else {
            log.info("Business: fetched {} users", users.size());
        }

        return users.stream()
                .map(mapper::toDto)
                .toList();
    }

    public UserDto getById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business: no user found with id {}", id);
                    return new UserNotFoundException(id);
                });

        log.info("Business: fetched user with id {}", id);
        return mapper.toDto(user);
    }

    public UserDto create(CreateUserRequest dto) {
        User user = mapper.toEntity(dto);
        User saved = repository.save(user);

        log.info("Business: created user with id {}", saved.getId());
        return mapper.toDto(saved);
    }

    public UserDto update(Long id, UpdateUserRequest dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business: no user found to update with id {}", id);
                    return new UserNotFoundException(id);
                });

        mapper.updateEntityFromDto(dto, user);

        User updated = repository.save(user);
        log.info("Business: updated user with id {}", updated.getId());

        return mapper.toDto(updated);
    }

    public void delete(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Business: no user found to delete with id {}", id);
                    return new UserNotFoundException(id);
                });

        repository.delete(user);
        log.info("Business: deleted user with id {}", id);
    }
}
