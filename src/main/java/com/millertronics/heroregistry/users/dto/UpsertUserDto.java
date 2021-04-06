package com.millertronics.heroregistry.users.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@JsonRootName("upsertUser")
@AllArgsConstructor
@Getter
@Builder
public class UpsertUserDto {

    private String username;
    private String alias;
    private String realName;
    private String bio;
}
