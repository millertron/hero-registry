package com.millertronics.heroregistry.authentication.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonRootName("authentication")
@AllArgsConstructor
@Getter
public class AuthenticationRequestDto {

    private final String username;
    private final String password;
}
