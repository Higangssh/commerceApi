package com.the.commerce.model.dto;


import com.the.commerce.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class MemberDto {
    private Long id;
    private String userId;
    private String userPwd;

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$")
    private String pNumber;


    private String nickName;
    private String userName;
    private String createAt;
    private String modifiedAt;

    public static MemberDto toDto(MemberEntity entity) {
        return MemberDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .userPwd(entity.getUserPwd())
                .userName(entity.getUserName())
                .email(entity.getEmail())
                .nickName(entity.getNickName())
                .pNumber(entity.getPNumber())
                .createAt(formattingFromCreateDate(entity))
                .modifiedAt(formattingFromModifiedDate(entity))
                .build();
    }

    public static String formattingFromCreateDate(MemberEntity memberEntity) {
        LocalDateTime createAt = memberEntity.getCreateAt();
        return createAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static String formattingFromModifiedDate(MemberEntity memberEntity) {
        LocalDateTime modifiedAt = memberEntity.getModifiedAt();
        return modifiedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    }

    public String encodePassword(String password) throws NoSuchAlgorithmException {
        // MessageDigest 객체 생성 (SHA-256 사용)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // 비밀번호 바이트 배열로 변환하여 해싱
        byte[] hash = digest.digest(password.getBytes());

        // 해싱된 바이트 배열을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
