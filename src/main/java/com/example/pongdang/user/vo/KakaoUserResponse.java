package com.example.pongdang.user.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoUserResponse {
    private KakaoAccount kakao_account; // 카카오 계정 정보

    @Getter
    @Setter
    public static class KakaoAccount {
        private KakaoProfile profile; // 프로필 정보
    }

    @Getter
    @Setter
    public static class KakaoProfile {
        private String nickname; // 닉네임 정보
    }
}
