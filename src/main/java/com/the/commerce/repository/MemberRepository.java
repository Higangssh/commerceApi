package com.the.commerce.repository;

import com.the.commerce.model.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    Page<MemberEntity> findAll(Pageable pageable);

    MemberEntity findByUserId(String userId);
}
