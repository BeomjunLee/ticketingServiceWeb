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
     * 회원가입 버튼 클릭
     */
    @GetMapping("/join")
    public String signUp() {
        return "/join";
    }

    /**
     * [회원] 가입 폼으로
     */
    @GetMapping("/joinMember")
    public String signUpMemberForm() {
        return "/joinMember";
    }

    /**
     * [관리자] 가입 폼으로
     */
    @GetMapping("/joinStore")
    public String signUpStoreForm() {
        return "/joinStore";
    }

    /**
     * [회원] 수정 폼으로
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
    @GetMapping("/updateMember")
    public String updateMemberForm() {
        return "/updateMember";
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
        model.addAttribute("message", "가입에 성공하였습니다");
        return "redirect:/main";
    }


    /**
     * [관리자] 가입
     */
    @PostMapping("/joinStoreOk")
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
                .address(storeAdminForm.getStreetAddress() + " " + storeAdminForm.getDetailAddress())   //주소 합치기
                .latitude(storeAdminForm.getStoreLatitude())
                .longitude(storeAdminForm.getStoreLongitude())
                .companyNumber(storeAdminForm.getStoreCompanyNumber())
                .member(member)
                .build();

        memberService.createStoreAdmin(member, store);

        model.addAttribute("message", "가입에 성공하였습니다");
        return "";
    }

    /**
     * [회원, 관리자] 정보보기
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
    @GetMapping("/myPage")
    public String myInfo(Principal principal, Model model) {
        Member member = memberService.findByUsername(principal.getName());
        if (member.getRole().equals(Role.USER)){    //회원
            MemberDto dto = MemberDto.builder()
                    .username(member.getUsername())
                    .name(member.getName())
                    .phoneNum(member.getPhoneNum())
                    .email(member.getEmail())
                    .point(member.getPoint())
                    .build();
            model.addAttribute("member", dto);   //회원이면 member객체
            return "/memberInfo";

            }else if (member.getRole().equals(Role.STORE_ADMIN)) {  //가게 관리자
            Store store = storeService.findStore(member.getUsername());
            StoreAdminDto dto = StoreAdminDto.builder()
                    .member_id(member.getId())
                    .member_username(member.getUsername())
                    .member_name(member.getName())
                    .member_phoneNum(member.getPhoneNum())
                    .member_email(member.getEmail())

                    .store_id(store.getId())
                    .store_name(store.getName())
                    .store_address(store.getAddress())
                    .store_phoneNum(store.getPhoneNum())
                    .store_companyNumber(store.getCompanyNumber())
                    .store_status(store.getStoreStatus().getStatus())
                    .build();
            model.addAttribute("storeAdmin", dto);   //가게 관리자면 member + store => storeAdmin객체 보냄
            return "";
        }
        return "/error";
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
            model.addAttribute("message", "수정되었습니다");
            return "";

        }else if (member.getRole().equals(Role.STORE_ADMIN)) {  //가게 관리자
            memberService.updateStoreAdmin(principal.getName(), storeForm);
            model.addAttribute("message", "수정되었습니다");
            return "";
        }

        model.addAttribute("message", "수정 실패하였습니다");
        return "";
    }
    /**
     * 비밀번호 변경 폼으로
     */
    @PreAuthorize("hasAuthority('USER') or hasAuthority('STORE_ADMIN')")
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
        model.addAttribute("message", "비밀번호가 변경되었습니다");
        return "";
    }


    /**
     * [회원] 번호표 보기
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myTickets")
    public String myTicket(Principal principal, Model model) {
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
        model.addAttribute("ticket", dto);
        return "";
    }

    /**
     * [회원] 번호표 취소
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/cancelTicket")
    public String cancelMyTicket(Principal principal, Model model) {
        storeService.cancelTicket(principal.getName());
        
        model.addAttribute("message", "번호표가 취소되었습니다");
        return "";
    }

    /**
     * [회원] 포인트 기부하기
     */
}
