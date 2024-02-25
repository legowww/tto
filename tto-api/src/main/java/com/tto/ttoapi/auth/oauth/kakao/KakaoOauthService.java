package com.tto.ttoapi.auth.oauth.kakao;

import com.tto.ttoapi.auth.oauth.OauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class KakaoOauthService {

    private final OauthProperties oauthKakaoProperties;
    private final KakaoAuthServerFeignClient kakaoAuthServerFeignClient;
    private final KakaoResourceServerFeignClient kakaoResourceServerFeignClient;

    public String getSocialType() {
        return oauthKakaoProperties.getKakao()
                .getAuthorizationServer();
    }


    public String requestToken(String code) {
        return kakaoAuthServerFeignClient
                .requestToken(getParamMap(code))
                .accessToken();
    }


    public String requestResource(String accessToken) {
        return kakaoResourceServerFeignClient
                .userInfoRequest(plusBearerType(accessToken))
                .getEmail();
    }


    private String plusBearerType(String accessToken) {
        return "Bearer " + accessToken;
    }


    private Map<String, String> getParamMap(String code) {
        return Map.of(
                "client_id", oauthKakaoProperties.getKakao().getClientId(),
                "redirect_uri", oauthKakaoProperties.getKakao().getRedirectUri(),
                "code", code,
                "grant_type", "authorization_code"
        );
    }
}
