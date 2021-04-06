package com.millertronics.heroregistry.registration;

import com.millertronics.heroregistry.registration.dto.RegisterUserDto;
import com.millertronics.heroregistry.users.models.UserEntity;
import com.millertronics.heroregistry.users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup")
@AllArgsConstructor
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody RegisterUserDto registerUserDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registerUserDto.getUsername());
        user.setAlias(registerUserDto.getAlias());
        user.setRealName(registerUserDto.getRealName());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        userRepository.save(user);
    }
}
