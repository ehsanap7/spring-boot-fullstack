package com.ehsan.dto;

import com.ehsan.model.enums.Gender;

import java.util.List;

public record CustomerRegistrationDto(
        String name,
        String email,
        String password,
        Integer age,
        Gender gender
) {
}