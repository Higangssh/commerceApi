package com.the.commerce.controller;

import com.the.commerce.model.dto.MemberDto;
import com.the.commerce.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;


    /**
     * @param dto creat_at과0 modified_at 은 parameter 로 불필요합니다. 백엔드 단에서 자동생성 됩니다
     * @return 요청성공시 201 , 제약조건 위반시 409, 이외의 처리 오류 500 과 오류 메시지
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody MemberDto dto) {
        return memberService.addMember(dto);
    }

    /**
     * @param page      시작 페이지 넘버는 0페이지입니다
     * @param pageSize
     * @param sortField 가입일 순으로 요청시 createAt 이걸로 요청 이름순으로 요청시 userName
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<MemberDto>> list(@RequestParam int page, @RequestParam int pageSize, @RequestParam String sortField) {
        return memberService.getList(page, pageSize, sortField);
    }

    /**
     * @param userId and Dto update 정보를 dto 형식으로
     * @return update 된 정보가 나갑니다
     */
    @PutMapping("/{userId}")
    public ResponseEntity<MemberDto> update(@PathVariable String userId, @RequestBody MemberDto dto) {
        return memberService.updateMember(userId, dto);
    }


}
