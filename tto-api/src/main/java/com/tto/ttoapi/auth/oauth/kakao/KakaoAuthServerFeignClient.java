package com.tto.ttoapi.auth.oauth.kakao;

import com.tto.ttoapi.auth.oauth.kakao.response.KakaoTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;


@FeignClient(
        name = "kakao-auth",
        url = "https://kauth.kakao.com/oauth/token"
)
public interface KakaoAuthServerFeignClient {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse requestToken(Map<String, ?> data);
}

