package com.KbAi.KbAi_server.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable=false, unique = true)
    private String email;

    @Column(nullable=false)
    private String password;

    private String refreshToken;

    @Column(nullable=false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    private User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    //UserDetail implement한 내용
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auth = new ArrayList<>();
        auth.add(new SimpleGrantedAuthority(role.name()));
        return auth;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
