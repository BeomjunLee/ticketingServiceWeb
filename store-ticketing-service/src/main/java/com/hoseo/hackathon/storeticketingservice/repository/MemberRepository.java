package com.hoseo.hackathon.storeticketingservice.repository;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.status.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    //모든 회원들 조회
    Page<Member> findAllByStatus(Pageable pageable, Boolean status);

    //탈퇴 회원 조회
    List<Member> findAllByRole(Role role);

    //전체 회원수
    int countByStatus(Boolean status);
    
    //아이디 중복검색
    int countByUsername(String username);

    //탈퇴한 회원들 검색
    List<Member> findAllByStatus(Boolean status);

    @Override
    void delete(Member entity);
}
