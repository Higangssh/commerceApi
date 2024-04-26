package com.the.commerce.service;

import com.the.commerce.model.dto.MemberDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface MemberService {
    ResponseEntity<String> addMember(MemberDto member);

    ResponseEntity<List<MemberDto>> getList(int page, int pageSize, String sortField);

    ResponseEntity<MemberDto> updateMember(String userId, MemberDto dto);
}
