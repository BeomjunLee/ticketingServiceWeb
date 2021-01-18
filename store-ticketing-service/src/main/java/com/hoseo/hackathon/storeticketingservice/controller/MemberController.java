package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MyTicketDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.MemberForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.StoreForm;
import com.hoseo.hackathon.storeticketingservice.domain.resource.MemberResource;
import com.hoseo.hackathon.storeticketingservice.domain.resource.MyTicketResource;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final StoreService storeService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity findMembers(Pageable pageable, PagedResourcesAssembler<MemberDto> assembler){
        Page<Member> findMembers = memberService.findAll(pageable);
        //Page<Member>를 Page<MemberDTO>로
        Page<MemberDto> members = findMembers.map(member -> new MemberDto(member));

        var memberResource = assembler.toModel(members, e -> new MemberResource(e));
        return ResponseEntity.ok(memberResource);
    }
    /**
     * [회원] 가입
     */
    @PostMapping("/new")
    public ResponseEntity signUpMember(@Valid @RequestBody MemberForm memberForm) {
        Member member = Member.builder()
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNum(memberForm.getPhoneNum())
                .email(memberForm.getEmail())
                .build();
        memberService.createMember(member);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("회원가입 성공")
                .build();
        URI createUri = linkTo(MemberController.class).slash("new").toUri();
        return ResponseEntity.created(createUri).body(response);
    }

    /**
     * [관리자] 가입
     */
    @PostMapping("/admin/new")
    public ResponseEntity signUpAdmin(@Valid @RequestBody MemberForm memberForm, @Valid @RequestBody StoreForm storeForm) {
        Member member = Member.builder()
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNum(memberForm.getPhoneNum())
                .email(memberForm.getEmail())
                .build();
        Store store = Store.builder()
                .name(storeForm.getName())
                .phoneNum(storeForm.getPhoneNum())
                .latitude(storeForm.getLatitude())
                .longitude(storeForm.getLongitude())
                .companyNumber(storeForm.getCompanyNumber())
                .build();

        memberService.createAdmin(member);
        storeService.createStore(store);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("관리자 가입 성공")
                .build();
        URI createUri = linkTo(MemberController.class).slash("admin/new").toUri();
        return ResponseEntity.created(createUri).body(response);
    }

    /**
     * [회원] 정보보기
     */

    /**
     * [회원] 정보 수정
     */

    /**
     * [회원] 번호표 보기
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/tickets")
    public ResponseEntity myTicket(Principal principal) {
        Ticket ticket = storeService.findMyTicket(principal.getName());
        Store store = storeService.findStoreById(ticket.getStore().getId());

        MyTicketDto dto = MyTicketDto.builder()
                .name(store.getName())
                .phoneNum(store.getPhoneNum())
                .notice(store.getNotice())
                .totalWaitingCount(store.getTotalWaitingCount())
                .peopleCount(ticket.getPeopleCount())
                .waitingNum(ticket.getWaitingNum())
                .waitingTime(ticket.getWaitingTime())
                .build();
        MyTicketResource resource = new MyTicketResource(dto);
        return ResponseEntity.ok(resource);
    }

    /**
     * [회원] 번호표 취소
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/tickets")
    public ResponseEntity cancelMyTicket(Principal principal) {
        storeService.cancelTicket(principal.getName());
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("번호표 취소 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * [회원] 포인트 기부하기
     */
}
