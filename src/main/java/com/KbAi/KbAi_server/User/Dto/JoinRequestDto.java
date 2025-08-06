package com.KbAi.KbAi_server.User.Dto;

import com.KbAi.KbAi_server.Entity.Role;
import com.KbAi.KbAi_server.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JoinRequestDto {
    private String email;
    private String password;
    private String name;
    private Role role;

    @Builder
    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(Role.ROLE_USER)
                .build();
    }

}
