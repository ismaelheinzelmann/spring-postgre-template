package com.template.demo.dto.request;

import static com.template.demo.constants.SystemMessages.*;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(@NotBlank(message = EMPTY_NAME) String name) {}
