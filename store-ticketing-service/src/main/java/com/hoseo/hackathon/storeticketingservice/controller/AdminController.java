package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.dto.*;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminMemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminMemberManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminStoreAdminDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminStoreManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.*;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreStatus;
import com.hoseo.hackathon.storeticketingservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/admin")
public class AdminController {
    private final AdminService adminService;

//========================================가게 관리============================================
    /**
     * 관리할 가게 리스트 보기
     * 응답 : 가게이름, 전화번호, 주소, 등록일, 등록된 가게 수
     * link : 가게 번호표 관리, 가게수정, 가게관리자 정보보기, 이름으로 검색, 주소로 검색
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores")
    public String findStores(@RequestParam(value = "page", defaultValue = "0") int page, Model model){
        int totalEnrollStoreCount = adminService.totalEnrollStoreCount();

        Pageable pageable = PageRequest.of(page, 10);
        AdminStoreManageDto dto = AdminStoreManageDto.builder()
                .storeList(adminService.findStores(StoreStatus.VALID, pageable))
                .totalEnrollStoreCount(totalEnrollStoreCount)
                .build();

        model.addAttribute("storeList", dto);
        return "/admin/stores";
    }

    /**
     * 대기중인 번호표 리스트 관리
     * 응답 : 대기중인 회원정보,  가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
     * link : self, 대기 보류 취소 체크,  가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/manageStore/{store_id}")
    public String manageAdminStore(@PathVariable("store_id") Long store_id, @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        Pageable pageable = PageRequest.of(page, 7);

        Store store = adminService.findStore(store_id);
        StoreManageDto dto = StoreManageDto.builder()
                .waitingMembers(adminService.findWaitingMembers(store_id, pageable))
                .storeTicketStatus(store.getStoreTicketStatus())
                .totalWaitingCount(store.getTotalWaitingCount())
                .totalWaitingTime(store.getTotalWaitingTime())
                .notice(store.getNotice())
                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
                .store_id(store.getId())
                //TODO 보류회원 결정나면 작업
                .build();
        model.addAttribute("manageStore", dto);
        return "/admin/manageStore";
    }
    /**
     * 보류된 번호표 리스트 관리
     * 응답 ; 이름, 전화번호
     * link : 보류 ticket별 취소, 체크,
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/holdTickets/{store_id}")
    public String manageHoldMembers(@RequestParam(value = "page", defaultValue = "0") int page, @PathVariable("store_id") Long store_id, Model model) {

        Pageable pageable = PageRequest.of(page, 5);

        Page<HoldingMembersDto> holdingMembers = adminService.findHoldMembers(store_id, pageable);
        model.addAttribute("holdingMembers", holdingMembers);
        return "/admin/manageStore";
    }

    /**
     * 번호표 보류
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/holdTicket/{ticket_id}")
    public String holdTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id, Model model) {
        adminService.holdTicket(store_id, ticket_id);
        model.addAttribute("message", "보류되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 번호표 취소
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/cancelTicket/{ticket_id}")
    public String cancelTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id, Model model) {
        adminService.cancelTicketByAdmin(store_id, ticket_id);
        model.addAttribute("message", "취소되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 번호표 체크
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/checkTicket/{ticket_id}")
    public String checkTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id, Model model) {
        adminService.checkTicket(store_id, ticket_id);
        model.addAttribute("message", "체크되었습니다");
        return "/admin/manageStore";
    }


    /**
     * 보류된 번호표 취소
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/cancelHoldTicket/{ticket_id}")
    public String holdCancelTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id, Model model) {
        adminService.holdCancelTicket(store_id, ticket_id);
        model.addAttribute("message", "취소되었습니다");
        return "/admin/manageStore";
    }
    /**
     * 보류된 번호표 체크
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/checkHoldTicket/{ticket_id}")
    public String holdCheckTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id, Model model) {
        adminService.holdCheckTicket(store_id, ticket_id);
        model.addAttribute("message", "체크되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 번호표 OPEN
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/open")
    public String openTicket(@PathVariable("store_id")Long store_id, Model model) {
        adminService.openTicket(store_id);
        model.addAttribute("message", "번호표가 활성화되었습니다");
        return "/admin/manageStore";
    }
    /**
     * 번호표 CLOSE
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/close")
    public String closeTicket(@PathVariable("store_id")Long store_id, Model model) {
        adminService.closeTicket(store_id);
        model.addAttribute("message", "번호표가 활성화되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 공지사항 수정
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/updateNotice")
    public String updateNotice(@PathVariable("store_id")Long store_id, @RequestBody @Valid StoreNoticeForm form, Model model) {
        adminService.updateStoreNotice(store_id, form.getNotice());

        model.addAttribute("message", "수정되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 한사람당 대기시간 수정
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/updateTime")
    public String updateAvgWaitingTime(@PathVariable("store_id")Long store_id, @RequestBody @Valid AvgTimeForm form, Model model) {
        adminService.updateAvgTime(store_id, form.getAvgWaitingTimeByOne());

        model.addAttribute("message", "수정되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 가게 관리자 수정
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/updateMember/{member_id}")
    public String updateStoreAdmin(@PathVariable("store_id")Long store_id, @PathVariable("member_id")Long member_id,
                                           @RequestBody @Valid AdminUpdateStoreAdminForm form, Model model) {
        adminService.updateStoreAdmin(store_id, member_id, form);
        model.addAttribute("message", "수정되었습니다");
        return "";
    }

    /**
     * 가게, 관리자 정보보기
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/storeAdminInfo/{member_id}")
    public String findStoreAdmin(@PathVariable("store_id")Long store_id, @PathVariable("member_id")Long member_id, Model model) {
        Member member = adminService.findStoreAdmin(member_id);
        Store store = adminService.findStore(store_id);
        AdminStoreAdminDto dto = AdminStoreAdminDto.builder()
                .member_username(member.getUsername())
                .member_name(member.getName())
                .member_phoneNum(member.getPhoneNum())
                .member_email(member.getEmail())
                .member_createdDate(member.getCreatedDate())
                .store_name(store.getName())
                .store_phoneNum(store.getPhoneNum())
                .store_address(store.getAddress())
                .store_companyNumber(store.getCompanyNumber())
                .store_createdDate(store.getCreatedDate())
                .build();
        model.addAttribute("storeAdmin", dto);
        return "";
    }
//==================================================회원 관리==============================================

    /**
     * 관리할 회원 리스트 보기
     * res : 총 회원수, 현재 서비스 이용자 수, 아이디, 이름, 전화번호, 이메일, 가입일, 포인트
     * 링크 : 가입일순으로보기, 이름순으로보기, 검색, 회원탈퇴, 회원수정, 번호표 취소
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/members")
    public String manageMembers(Model model, @RequestParam(value = "page", defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 10);

        //회원 리스트
        AdminMemberManageDto dto = AdminMemberManageDto.builder()
                .totalMemberCount(adminService.totalMemberCount())
                .currentUsingServiceCount(adminService.currentUsingServiceCount())
                .memberList(adminService.findMembers(pageable))
                .build();
        model.addAttribute("memberList", dto);
        return "";
    }

    /**
     * 회원 탈퇴
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteMember/{member_id}")
    public String deleteMember(@PathVariable("member_id")Long member_id, Model model) {
        adminService.deleteMember(member_id);
        model.addAttribute("message", "탈퇴되었습니다");
        return "";
    }

    /**
     * 회원 정보 보기
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/memberInfo/{member_id}")
    public String updateMember(@PathVariable("member_id")Long member_id, Model model) {
        Member member = adminService.findMember(member_id);
        AdminMemberDto dto = AdminMemberDto.builder()
                .member_id(member.getId())
                .username(member.getUsername())
                .name(member.getName())
                .phoneNum(member.getPhoneNum())
                .email(member.getEmail())
                .point(member.getPoint())
                .createdDate(member.getCreatedDate())
                .build();

        model.addAttribute("member", dto);
        return "";
    }


    /**
     * 회원 수정
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/updateMember/{member_id}")
    public String updateMember(@PathVariable("member_id")Long member_id, @RequestBody @Valid AdminUpdateMemberForm form, Model model) {
        adminService.updateMember(member_id, form);

        model.addAttribute("message", "수정되었습니다");
        return "";
    }

    /**
     * 비밀번호 변경 폼으로
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/updatePassword/")
    public String updatePasswordForm() {
        return "/admin/updatePassword";
    }

    /**
     * 비밀번호 변경
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/updatePasswordOk/{member_id}")
    public String changePassword(@PathVariable("member_id")Long member_id, @RequestBody @Valid UpdatePasswordForm form, Model model) {
        adminService.changePassword(member_id, form.getCurrentPassword(), form.getNewPassword());
        model.addAttribute("message", "비밀번호가 변경되었습니다");
        return "";
    }


    /**
     * 탈퇴후 7일지난 회원 영구삭제
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/deleteMembers")
    public String deleteMembersWeekPast(Model model) {
        adminService.deleteMemberWeekPast();
        model.addAttribute("message", "영구삭제 처리되었습니다");
        return "";
    }

    /**
     * 번호표 취소
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/cancelTicket/{ticket_id}")
    public String manageMembers(@PathVariable("ticket_id")Long ticket_id, Model model) {
        //회원 번호표 취소
        adminService.cancelTicket(ticket_id);
        model.addAttribute("message", "번호표가 취소되었습니다");
        return "";
    }

//===========================================가입 승인===================================================
    /**
     * 가입 승인 가게 목록
     * 링크 : 가게 관리자 정보, 가입 승인, 이름으로 검색, 주소로 검색
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/waitStore")
    public String findStoresWaitingToJoin(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10);

        int totalEnrollStoreCount = adminService.totalEnrollStoreCount();
        AdminStoreManageDto dto = AdminStoreManageDto.builder()
                .storeList(adminService.findStores(StoreStatus.INVALID, pageable))
                .totalEnrollStoreCount(totalEnrollStoreCount)
                .build();
        model.addAttribute("storeList", dto);
        return "";
    }


    /**
     * 가입 승인
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/applyJoin/{member_id}")
    public String permitStore(@PathVariable("store_id") Long store_id, @PathVariable("member_id") Long member_id, Model model) {
        adminService.permitStoreAdmin(member_id, store_id);
        model.addAttribute("message", "가입 승인되었습니다");
        return "";
    }

    /**
     * 가입 승인 취소
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/cancelJoin/{member_id}")
    public String rejectStore(@PathVariable("store_id") Long store_id, @PathVariable("member_id") Long member_id, Model model) {
        adminService.rejectStoreAdmin(member_id, store_id);
        model.addAttribute("message", "가입 승인이 취소되었습니다");
        return "";
    }

//=============================================시스템 에러=============================================
    /**
     * 오류 접수
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/applyError")
    public String sendErrorSystem(@PathVariable("store_id")Long store_id, Model model) {
        adminService.sendErrorSystem(store_id);

        model.addAttribute("message", "오류가 접수되었습니다");
        return "/admin/manageStore";
    }

    /**
     * 시스템 장애 복구 완료
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/stores/{store_id}/completeError")
    public String errorSystemComplete(@PathVariable("store_id") Long store_id, Model model) {
        adminService.completeErrorSystem(store_id);
        model.addAttribute("message", "정상 처리되었습니다");
        return "";
    }

    /**
     * 시스템 장애 목록
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/errorStores")
    public String errorSystemList() {
        return "";
    }

    /**
     * 시스템 장애 목록 대기인원순
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/errorStores/wait")
    public String errorSystemListByWaiting() {
        return "";
    }
}
