package com.millertronics.heroregistry.registration.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonRootName("registerUser")
@Builder
@Getter
@AllArgsConstructor
public class RegisterUserDto {

    private final String username;
    private final String alias;
    private final String realName;
    private final String password;
}
