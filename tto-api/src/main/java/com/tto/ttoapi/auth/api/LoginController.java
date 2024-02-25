package com.tto.core.auth.api;

import com.tto.ttoapi.auth.oauth.OauthProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/login")
@RestController
public class LoginController {

    private final OauthProperties oauthProperties;

    @GetMapping("/kakao")
    public void loginKakao(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect(oauthProperties.getKakao().getLoginPageRedirectUri());
    }
}
