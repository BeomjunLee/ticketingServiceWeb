package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MyTicketDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreAdminDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.*;

import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.domain.status.Role;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final StoreService storeService;

    /**
     * [회원] 가입 폼으로
     */
    @GetMapping("/joinMember")
    public String signUpMemberForm() {
        return "/joinMember";
    }

    /**
     * [회원] 가입
     */
    @PostMapping("/joinMemberOk")
    public String signUpMember(@Valid MemberForm memberForm, Model model) {
        Member member = Member.builder()
                .username(memberForm.getUsername())
                .password(memberForm.getPassword())
                .name(memberForm.getName())
                .phoneNum(memberForm.getPhoneNum())
                .email(memberForm.getEmail())
                .createdDate(LocalDateTime.now())
                .deletedDate(null)  //탈퇴일은 가입시 null(재가입시 null 로 바꿔야돼서)
                .build();
        memberService.createMember(member);
        model.addAttribute("message", "회원가입 성공");
        return "/main";
    }

    /**
     * [관리자] 가입
     */
    @PostMapping("/joinStoreAdmin")
    public String signUpAdmin(@Valid StoreAdminForm storeAdminForm, Model model) {
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
                .member(member)
                .build();

        memberService.createStoreAdmin(member, store);

        model.addAttribute("message", "회원가입 성공");
        return "";
    }

    /**
     * [회원] 정보보기
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
    @GetMapping("/memberInfo")
    public String myInfo(Principal principal, Model model) {
        Member member = memberService.findByUsername(principal.getName());
        if (member.getRole().equals(Role.USER)){    //회원

            model.addAttribute("member", member);   //회원이면 member객체

            }else if (member.getRole().equals(Role.STORE_ADMIN)) {  //가게 관리자
            Store store = storeService.findStore(member.getUsername());

            model.addAttribute("member", member);   //가게 관리자면 member, store 객체 보냄
            model.addAttribute("store", store);
        }
        return "/memberInfo";
    }

    /**
     * [회원] 수정 폼으로
     */
    @GetMapping("/updateMember")
    public String updateMemberForm() {
        return "/updateMember";
    }


    /**
     * [회원] 정보 수정
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
    @PostMapping("/updateMemberOk")
    public String updateMyInfo(Principal principal, Model model,
                                        @Valid UpdateMemberForm memberForm,
                                        @Valid UpdateStoreAdminForm storeForm) {
        Member member = memberService.findByUsername(principal.getName());
        if (member.getRole().equals(Role.USER)){    //회원
            memberService.updateMember(principal.getName(), memberForm);
            model.addAttribute("message", "수정 성공");
            return "";

        }else if (member.getRole().equals(Role.STORE_ADMIN)) {  //가게 관리자
            memberService.updateStoreAdmin(principal.getName(), storeForm);
            model.addAttribute("message", "수정 성공");
            return "";
        }

        model.addAttribute("message", "수정 실패");
        return "";
    }
    /**
     * 비밀번호 변경 폼으로
     */
    @GetMapping("/updatePassword")
    public String updatePasswordForm() {
        return "/updatePassword";
    }

    /**
     * 비밀번호 변경
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
    @PostMapping("/updatePasswordOk")
    public String changePassword(Principal principal, @RequestBody @Valid UpdatePasswordForm form, Model model) {
        memberService.changePassword(principal.getName(), form.getCurrentPassword(), form.getNewPassword());
        model.addAttribute("message", "수정 실패");
        return "";
    }


    /**
     * [회원] 번호표 보기
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/tickets")
    public String myTicket(Principal principal) {
        Ticket ticket = storeService.findMyTicket(principal.getName());
        Store store = storeService.findValidStoreById(ticket.getStore().getId());
        
        MyTicketDto dto = MyTicketDto.builder()
                .name(store.getName())
                .phoneNum(store.getPhoneNum())
                .notice(store.getNotice())
                .totalWaitingCount(store.getTotalWaitingCount())
                .peopleCount(ticket.getPeopleCount())
                .waitingNum(ticket.getWaitingNum())
                .waitingTime(ticket.getWaitingTime())
                .build();
        return "";
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
