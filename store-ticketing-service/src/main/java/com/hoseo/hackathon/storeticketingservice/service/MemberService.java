package com.hoseo.hackathon.storeticketingservice.service;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.MemberStatus;
import com.hoseo.hackathon.storeticketingservice.domain.Role;
import com.hoseo.hackathon.storeticketingservice.exception.DuplicateUsernameException;
import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
@Transactional(readOnly = true) //조회최적화
@RequiredArgsConstructor    //스프링 주입
public class MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인체크
     */
    public Member loginCheck(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치 하지않습니다");
        } else return member;
    }

    /**
     * [회원] 회원가입
     */
    @Transactional //조회가 아니므로 Transactional
    public Member createMember(Member member) {
        validateDuplicateMember(member.getUsername()); //중복회원검증
        member.changeRole(Role.USER);   //권한부여
        member.changeMemberStatus(MemberStatus.VALID);  //일반 회원은 바로 가입
        //비밀번호 encoding
        member.encodingPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    /**
     * [관리자] 회원가입
     */
    @Transactional //조회가 아니므로 Transactional
    public Member createAdmin(Member member) {
        validateDuplicateMember(member.getUsername()); //중복회원검증
        member.changeRole(Role.STORE_ADMIN); //권한부여
        member.changeMemberStatus(MemberStatus.INVALID); //가게 관리자는 가입 대기상태
        //비밀번호 encoding
        member.encodingPassword(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }

    /**
     * 중복 회원 검증
     */
    private void validateDuplicateMember(String username) {
        Long findMembers = memberRepository.countByUsername(username);
        if (findMembers > 0) {
            throw new DuplicateUsernameException("아이디가 중복되었습니다");
        }
        //두 유저가 동시에 가입할 경우를 대비해서 DB 에도 유니크 제약조건을 걸어줘야함
    }

    /**
     * 회원 수정
     */
    @Transactional
    public Member update(Member member) {
        return memberRepository.save(member);
    }

    /**
     * 회원 정보 보기
     */
    public Member findOne(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("해당되는 유저를 찾을수 없습니다"));
    }

    /**
     * 회원 전체 조회
     */
    public Page<Member> findAll(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    /**
     * 포인트 기부하기
     */

    /**
     * [사이트 관리자] 가게 관리자 가입 승인
     */
}
