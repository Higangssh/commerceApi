package com.the.commerce.service;

import com.the.commerce.controller.MemberController;
import com.the.commerce.model.dto.MemberDto;
import com.the.commerce.model.entity.MemberEntity;
import com.the.commerce.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
class MemberServiceImplTest {


    private static final Logger log = LoggerFactory.getLogger(MemberServiceImplTest.class);
    @Autowired
    private MemberController controller;
    @Autowired
    private MemberService realService;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;


    @Test
    @DisplayName("유효한 맴버추가")
    void addMember_ValidRequest() {
        MemberDto dto = MemberDto.builder()
                .userId("새로운 가입자")
                .userPwd("password")
                .userName("강릉")
                .email("sg3333@naver.com")
                .pNumber("010-3040-5959")
                .nickName("새로운")
                .build();

        when(memberService.addMember(dto))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body("Member added successfully"));
        ResponseEntity<String> response = controller.join(dto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("중복 맴버 추가")
    void addMember_DuplicateRequest() {
        MemberDto dto = MemberDto.builder()
                .userId("중복 가입자")
                .userPwd("password")
                .userName("강릉")
                .email("sg3333@naver.com")
                .pNumber("010-3040-5959")
                .nickName("중복")
                .build();

        when(memberService.addMember(dto))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to add member: constraint violation"));
        ResponseEntity<String> response = controller.join(dto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("전화번호 유효성 틀린 사람 검사")
    void addMember_InvalidPhoneNumber() {
        MemberDto dto = MemberDto.builder()
                .userId("새로운 가입자")
                .userPwd("password")
                .userName("강릉")
                .email("sg33313@naver.com")
                .pNumber("12345")  // 유효하지 않은 전화번호
                .nickName("새로운1")
                .build();
        when(controller.join(dto))
                .thenReturn(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invaild phoneNumber"));
        ResponseEntity<String> response = controller.join(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("업데이트 확인")
    void updateMember() {
        MemberEntity entity = new MemberEntity(1L, "과거", "password1", "User 1", "010-1234-5678", "nickname1", "user1@example.com");
        MemberDto dto = new MemberDto(1L, "새로운", "password1", "User 1", "010-1234-5678", "nickname1", "user1@example.com", "aa", "aa");
        String userId = entity.getUserId();
        when(memberRepository.findByUserId(userId)).thenReturn(entity);
        when(realService.updateMember(userId, dto)).thenReturn(ResponseEntity.ok(dto));

        ResponseEntity<MemberDto> response = realService.updateMember(userId, dto);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getUserId()).isEqualTo("새로운");
        assertThat(Objects.requireNonNull(realService.updateMember(userId, dto).getBody()).getUserId()).isEqualTo("새로운");
    }

    @Test
    @DisplayName("회원 업데이트 과정에서 예외가 발생하는 경우")
    void updateMember_notFound() {
        String userId = "existingUser";
        MemberDto dto = new MemberDto();
        when(memberRepository.findByUserId(userId)).thenReturn(null); // null을 반환하도록 설정

        // MemberRepository가 null을 반환할 때 예외 처리
        ResponseEntity<MemberDto> response = null;
        try {
            response = realService.updateMember(userId, dto);
        } catch (Exception e) {
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("getList 정상확인 ")
    void getList_Test() {
        Pageable pageable = PageRequest.of(0, 2, Sort.by("userName"));
        when(memberService.getList(0, 2, "userName"))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());
        ResponseEntity<List<MemberDto>> response = controller.list(0, 2, "userName");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}