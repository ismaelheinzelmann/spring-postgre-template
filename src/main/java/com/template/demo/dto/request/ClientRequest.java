package com.template.demo.dto.request;

import static com.template.demo.constants.SystemMessages.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequest {

    @NotBlank(message = EMPTY_NAME)
    private String name;

    @NotBlank(message = EMPTY_CPF)
    @Pattern(regexp = "\\d{11}", message = INVALID_CPF)
    private String cpf;

    @Email(message = INVALID_EMAIL)
    private String email;
}
