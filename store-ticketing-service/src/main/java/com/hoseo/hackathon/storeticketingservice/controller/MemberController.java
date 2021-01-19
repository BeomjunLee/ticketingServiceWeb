package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MyTicketDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.MemberForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.StoreAdminForm;
import com.hoseo.hackathon.storeticketingservice.domain.resource.MyTicketResource;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/members", produces = MediaTypes.HAL_JSON_VALUE)
public class MemberController {

    private final MemberService memberService;
    private final StoreService storeService;


    /**
     * [회원] 가입
     */
    @ApiOperation(value = "일반 회원 가입[누구나]", notes = "회원을 서비스에 가입시킵니다")
    @PostMapping("/new")
    public ResponseEntity signUpMember(@Valid @RequestBody MemberForm memberForm) {
        Member member = Member.builder()
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNum(memberForm.getPhoneNum())
                .email(memberForm.getEmail())
                .createdDate(LocalDateTime.now())
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
    @ApiOperation(value = "가게 관리자 회원 가입[누구나]", notes = "가게 사장님을 서비스에 가입시킵니다")
    @PostMapping("/admin/new")
    public ResponseEntity signUpAdmin(@Valid @RequestBody StoreAdminForm storeAdminForm) {
        Member member = Member.builder()//회원
                .username(storeAdminForm.getMemberUsername())
                .password(storeAdminForm.getMemberPassword())
                .name(storeAdminForm.getMemberName())
                .phoneNum(storeAdminForm.getMemberPhoneNum())
                .email(storeAdminForm.getMemberEmail())
                .createdDate(LocalDateTime.now())
                .build();
        Store store = Store.builder()//가게
                .name(storeAdminForm.getStoreName())
                .phoneNum(storeAdminForm.getStorePhoneNum())
                .address(storeAdminForm.getStoreAddress())
                //TODO 주소를 위도, 경도로 바꿔야됨
                .latitude(storeAdminForm.getStoreLatitude())
                .longitude(storeAdminForm.getStoreLongitude())
                .companyNumber(storeAdminForm.getStoreCompanyNumber())
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
    @ApiOperation(value = "번호표 조회[회원]", notes = "가게 사장님을 서비스에 가입시킵니다")
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
    @ApiOperation(value = "번호표 취소[회원]", notes = "뽑은 번호표를 취소합니다")
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
