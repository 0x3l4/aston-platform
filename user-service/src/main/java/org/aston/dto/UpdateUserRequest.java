package org.aston.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Schema(description = "Запрос на обновление пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    @Schema(description = "Имя пользователя", example = "Ivan Ivanov")
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Возраст пользователя", example = "30")
    @Min(13)
    @Max(130)
    private int age;
}

