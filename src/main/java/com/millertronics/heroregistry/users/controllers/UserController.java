package com.millertronics.heroregistry.users.controllers;

import com.millertronics.heroregistry.users.dto.BaseUserDto;
import com.millertronics.heroregistry.users.dto.UpsertUserDto;
import com.millertronics.heroregistry.users.models.UserEntity;
import com.millertronics.heroregistry.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/")
    public List<BaseUserDto> list() {
        return userRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public BaseUserDto create(@RequestBody UpsertUserDto userDto) {
        UserEntity userEntity = convertDtoToEntity(userDto);
        return convertEntityToDto(userRepository.save(userEntity));
    }

    private UserEntity convertDtoToEntity(UpsertUserDto userDto) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setRealName(userDto.getRealName());
        userEntity.setAlias(userDto.getAlias());
        userEntity.setBio(userDto.getBio());

        return userEntity;
    }

    private BaseUserDto convertEntityToDto(UserEntity userEntity) {
        return BaseUserDto.builder().id(userEntity.getId())
                .username(userEntity.getUsername())
                .realName(userEntity.getRealName())
                .alias(userEntity.getAlias())
                .realName(userEntity.getRealName())
                .bio(userEntity.getBio())
                .build();
    }

    @GetMapping("/{userId}")
    public BaseUserDto find(@PathVariable Integer userId) {
        return convertEntityToDto(userRepository.findById(userId).get());
    }

    @PutMapping("/{userId}")
    public BaseUserDto update(@PathVariable Integer userId, @RequestBody UpsertUserDto userDto) {
        UserEntity userEntity = userRepository.findById(userId).get();
        userEntity.setUsername(userDto.getUsername());
        userEntity.setAlias(userDto.getAlias());
        userEntity.setRealName(userDto.getRealName());
        userEntity.setBio(userDto.getBio());

        return convertEntityToDto(userRepository.save(userEntity));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId) {
        userRepository.delete(userRepository.findById(userId).get());
    }

}
