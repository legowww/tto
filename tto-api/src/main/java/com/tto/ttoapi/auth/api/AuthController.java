package com.tto.core.auth.api;


import com.tto.ttoapi.auth.oauth.kakao.KakaoOauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final KakaoOauthService kakaoOauthService;

    @GetMapping("/kakao/callback")
    public void kakaoRedirectURI(@RequestParam String code) {
        String accessToken = kakaoOauthService.requestToken(code);
        String email = kakaoOauthService.requestResource(accessToken);

        log.info("email = {}", email);
    }
}
