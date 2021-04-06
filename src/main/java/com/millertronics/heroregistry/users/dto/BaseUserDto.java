package com.millertronics.heroregistry.users.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonRootName("user")
@AllArgsConstructor
@Getter
@Builder
public class BaseUserDto {
    private final int id;
    private final String username;
    private final String alias;
    private final String realName;
    private final String bio;
}
