package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.resource.MemberResource;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;

    //전체 회원 검색
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity findMembers(Pageable pageable, PagedResourcesAssembler<MemberDto> assembler){
        Page<Member> findMembers = memberService.findAll(pageable);
        //Page<Member>를 Page<MemberDTO>로
        Page<MemberDto> members = findMembers.map(member -> new MemberDto(member));

        var memberResource = assembler.toModel(members, e -> new MemberResource(e));
        return ResponseEntity.ok(memberResource);
    }

}
