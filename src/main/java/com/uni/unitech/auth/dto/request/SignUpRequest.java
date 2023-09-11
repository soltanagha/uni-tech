package com.uni.unitech.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "PIN is required")
    private String pin;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password must contain only alphanumeric characters")
    private String password;
}
