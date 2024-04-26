package com.the.commerce.model.entity;

import com.the.commerce.model.dto.MemberDto;
import lombok.*;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(
        name = "member_tbl",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "nick_unique_fk",
                        columnNames = {"nickName"}
                ),
                @UniqueConstraint(
                        name = "email_unique_fk",
                        columnNames = {"email"}
                )
        }
)
public class MemberEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_pwd")
    private String userPwd;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "p_number")
    private String pNumber;
    @Column(unique = true)
    private String nickName;
    @Column(unique = true)
    private String email;

    public static MemberEntity toEntity(MemberDto dto) throws NoSuchAlgorithmException {
        return MemberEntity.builder()
                .id(dto.getId())
                .userId(dto.getUserId())
                .userPwd(dto.encodePassword(dto.getUserPwd()))
                .nickName(dto.getNickName())
                .userName(dto.getUserName())
                .pNumber(dto.getPNumber())
                .email(dto.getEmail())
                .build();
    }

    public void update(MemberDto dto) {
        if (dto.getUserPwd() != null) {
            setUserPwd(dto.getUserPwd());
        }
        if (dto.getNickName() != null) {
            setNickName(dto.getNickName());
        }
        if (dto.getUserName() != null) {
            setUserName(dto.getUserName());
        }
        if (dto.getEmail() != null) {
            setEmail(dto.getEmail());
        }
        if (dto.getPNumber() != null) {
            setPNumber(dto.getPNumber());
        }
    }
}
