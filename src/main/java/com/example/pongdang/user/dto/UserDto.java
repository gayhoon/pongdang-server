package com.example.pongdang.user.dto;

import com.example.pongdang.user.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;
    private String email;
    private String nickname;

    public UserEntity toEntity(){
        return UserEntity.builder()
                .email(this.email)
                .nickname(this.nickname)
                .build();
    }
}
