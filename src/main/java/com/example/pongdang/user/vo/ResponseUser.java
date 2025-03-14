package com.example.pongdang.user.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUser {

    private Long id;
    private String email;
    private String nickname;
}
