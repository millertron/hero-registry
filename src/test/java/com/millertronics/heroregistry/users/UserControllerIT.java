package com.millertronics.heroregistry.users;

import com.millertronics.heroregistry.users.models.UserEntity;
import com.millertronics.heroregistry.users.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @MockBean
    UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void create_success_returnsCreatedUser() throws Exception {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setUsername("username");
        userEntity.setAlias("alias");
        userEntity.setRealName("realName");
        userEntity.setBio("bio");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"upsertUser\": { \"username\": \"username\", \"alias\": \"alias\", \"realName\": \"realName\", \"bio\": \"bio\" } }"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.user.username").value("username"))
                .andExpect(jsonPath("$.user.alias").value("alias"))
                .andExpect(jsonPath("$.user.realName").value("realName"))
                .andExpect(jsonPath("$.user.bio").value("bio"));

    }
}
