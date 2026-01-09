package org.aston.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Запрос на создание пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {

    @Schema(description = "Имя пользователя", example = "Ivan Ivanov")
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Возраст пользователя", example = "25")
    @Min(13)
    @Max(130)
    private int age;
}
