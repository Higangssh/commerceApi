package com.the.commerce.service;

import com.the.commerce.model.dto.MemberDto;
import com.the.commerce.model.entity.MemberEntity;
import com.the.commerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;

    @Override
    public ResponseEntity<String> addMember(MemberDto member) {
        try {
            MemberEntity entity = memberRepository.save(MemberEntity.toEntity(member));
            log.info("{}", entity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Member added successfully");
        } catch (DataIntegrityViolationException e) {
            // 제약 조건 위반 시 예외 처리
            log.error("Failed to add member due to constraint violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Failed to add member: constraint violation");
        } catch (Exception e) {
            // 그 외 예외 처리
            log.error("Failed to add member: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add member");
        }
    }

    @Override
    public ResponseEntity<List<MemberDto>> getList(int page, int pageSize, String sortField) {
        try {
            Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sortField));
            List<MemberEntity> entities = memberRepository.findAll(pageable).getContent();

            if (entities.isEmpty()) {
                log.info("No members found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            log.info("Members retrieved successfully.");
            return ResponseEntity.status(HttpStatus.OK).body(entities.stream().map(MemberDto::toDto).collect(Collectors.toList()));
        } catch (Exception e) {
            log.error("Error occurred while retrieving members: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<MemberDto> updateMember(String userId, MemberDto dto) {
        try {
            MemberEntity memberEntity = memberRepository.findByUserId(userId);
            if (memberEntity == null) {
                log.info("No members found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            memberEntity.update(dto);
            log.info("Updating member: {}", memberEntity);
            return ResponseEntity.status(HttpStatus.OK).body(MemberDto.toDto(memberEntity));
        } catch (Exception e) {
            log.error("Error occurred while retrieving members: {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}

