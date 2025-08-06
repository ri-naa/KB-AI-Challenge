package com.KbAi.KbAi_server.User.Service;

import com.KbAi.KbAi_server.Entity.User;
import com.KbAi.KbAi_server.Repository.UserRepository;
import com.KbAi.KbAi_server.User.Dto.JoinRequestDto;
import com.KbAi.KbAi_server.User.Dto.LoginRequestDto;
import com.KbAi.KbAi_server.User.Dto.TokenResponseDto;
import com.KbAi.KbAi_server.User.Jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public Long join(JoinRequestDto requestDto) {
        //이메일 중복 검사
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "중복된 이메일입니다");
        }

        User user = requestDto.toEntity();
        user.encodePassword(passwordEncoder);
        userRepository.save(user);

        return user.getId();
    }

    public TokenResponseDto login(LoginRequestDto requestDto){
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"잘못된 이메일입니다"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 비밀번호입니다");
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getUsername(), user.getRole().name()); //여기가 문제인가?
        String refreshToken = jwtTokenProvider.createRefreshToken();

        user.updateRefreshToken(refreshToken); //refresh token 저장
        userRepository.save(user);

        return TokenResponseDto.builder()
                .grantType("Bearer")
                .jwtAccessToken(accessToken)
                .jwtRefreshToken(refreshToken)
                .build();

    }

    public TokenResponseDto issueAccessToken(HttpServletRequest request){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);

        if (!jwtTokenProvider.validAccessToken(accessToken)){
            log.info("access token 만료");

            if (jwtTokenProvider.validRefreshToken(refreshToken)){
                log.info("refresh token 유효");

                User user = userRepository.findByEmail(jwtTokenProvider.getUserEmail(refreshToken))
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 이메일입니다"));

                //refresh token 값이 같다면
                if (refreshToken.equals(user.getRefreshToken())){
                    accessToken = jwtTokenProvider.createAccessToken(user.getEmail(), user.getRole().name());
                }else {
                    log.info("refresh token 변조");
                }
            }else{
                log.info("refresh token 유효하지 않음");
            }
        }
        return TokenResponseDto.builder()
                .jwtAccessToken(accessToken)
                .jwtRefreshToken(refreshToken)
                .build();
    }
}
