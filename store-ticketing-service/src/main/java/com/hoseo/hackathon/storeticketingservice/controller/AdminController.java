package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.dto.*;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminMemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminMemberManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminStoreAdminDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.admin.AdminStoreManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.AdminUpdateMemberForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.AdminUpdateStoreAdminForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.AvgTimeForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.StoreNoticeForm;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreStatus;
import com.hoseo.hackathon.storeticketingservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores")
//    public String findStores(Pageable pageable, PagedResourcesAssembler<StoreListDto> assembler) {
//        int totalEnrollStoreCount = adminService.totalEnrollStoreCount();
//        AdminStoreManageDto dto = AdminStoreManageDto.builder()
////                .storeList(assembler.toModel(adminService.findStores(StoreStatus.VALID, pageable)))
//                .totalEnrollStoreCount(totalEnrollStoreCount)
//                .build();
//        //hateoas
//        return "";
//    }
//
//    /**
//     * 대기중인 번호표 리스트 관리
//     * 응답 : 대기중인 회원정보,  가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
//     * link : self, 대기 보류 취소 체크,  가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/{store_id}")
//    public String manageAdminStore(@PathVariable("store_id") Long store_id, Pageable pageable, PagedResourcesAssembler<WaitingMembersDto> assembler) {
//        Store store = adminService.findStore(store_id);
//        StoreManageDto dto = StoreManageDto.builder()
////                .waitingMembers(assembler.toModel(adminService.findWaitingMembers(store_id, pageable)))
//                .storeTicketStatus(store.getStoreTicketStatus())
//                .totalWaitingCount(store.getTotalWaitingCount())
//                .totalWaitingTime(store.getTotalWaitingTime())
//                .notice(store.getNotice())
//                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
//                .store_id(store.getId())
//                //TODO 보류회원 결정나면 작업
//                .build();
//        //hateoas
//        return "";
//    }
//    /**
//     * 보류된 번호표 리스트 관리
//     * 응답 ; 이름, 전화번호
//     * link : 보류 ticket별 취소, 체크,
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/{store_id}/hold")
//    public String manageHoldMembers(@PathVariable("store_id") Long store_id, Pageable pageable) {
//        //보류회원정보
////        PagedModel<EntityModel<HoldingMembersDto>> holdingMembers =
////                assembler.toModel(adminService.findHoldMembers(store_id, pageable));
//
//        return "";
//    }
//
//    /**
//     * 번호표 보류
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/tickets/{ticket_id}")
//    public String holdTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id) {
//        adminService.holdTicket(store_id, ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 보류 성공")
//                .build();
//        return "";
//    }
//
//    /**
//     * 번호표 취소
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/stores/{store_id}/tickets/{ticket_id}")
//    public ResponseEntity cancelTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id) {
//        adminService.cancelTicketByAdmin(store_id, ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 번호표 체크
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/stores/{store_id}/tickets/{ticket_id}")
//    public ResponseEntity checkTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id) {
//        adminService.checkTicket(store_id, ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 체크 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 보류된 번호표 취소
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/stores/{store_id}/tickets/{ticket_id}/hold")
//    public ResponseEntity holdCancelTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id) {
//        adminService.holdCancelTicket(store_id, ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("보류 번호표 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//    /**
//     * 보류된 번호표 체크
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/stores/{store_id}/tickets/{ticket_id}/hold")
//    public ResponseEntity holdCheckTicket(@PathVariable("ticket_id")Long ticket_id, @PathVariable("store_id")Long store_id) {
//        adminService.holdCheckTicket(store_id, ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("보류 번호표 체크 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 번호표 OPEN
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/stores/{store_id}/status")
//    public ResponseEntity openTicket(@PathVariable("store_id")Long store_id) {
//        adminService.openTicket(store_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 번호표 활성화 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//    /**
//     * 번호표 CLOSE
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/status")
//    public ResponseEntity closeTicket(@PathVariable("store_id")Long store_id) {
//        adminService.closeTicket(store_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 번호표 비활성화 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 공지사항 수정
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/notice")
//    public ResponseEntity updateNotice(@PathVariable("store_id")Long store_id, @RequestBody @Valid StoreNoticeForm form) {
//        adminService.updateStoreNotice(store_id, form.getNotice());
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("공지사항 변경 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 한사람당 대기시간 수정
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/time")
//    public ResponseEntity updateAvgWaitingTime(@PathVariable("store_id")Long store_id, @RequestBody @Valid AvgTimeForm form) {
//        adminService.updateAvgTime(store_id, form.getAvgWaitingTimeByOne());
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("한사람당 대기시간 변경 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 가게 관리자 수정
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/members/{member_id}")
//    public ResponseEntity updateStoreAdmin(@PathVariable("store_id")Long store_id, @PathVariable("member_id")Long member_id,
//                                           @RequestBody @Valid AdminUpdateStoreAdminForm form) {
//        adminService.updateStoreAdmin(store_id, member_id, form);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 관리자 수정 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 가게, 관리자 정보보기
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/{store_id}/members/{member_id}")
//    public ResponseEntity findStoreAdmin(@PathVariable("store_id")Long store_id, @PathVariable("member_id")Long member_id) {
//        Member member = adminService.findStoreAdmin(member_id);
//        Store store = adminService.findStore(store_id);
//        AdminStoreAdminDto dto = AdminStoreAdminDto.builder()
//                .member_username(member.getUsername())
//                .member_name(member.getName())
//                .member_phoneNum(member.getPhoneNum())
//                .member_email(member.getEmail())
//                .member_createdDate(member.getCreatedDate())
//                .store_name(store.getName())
//                .store_phoneNum(store.getPhoneNum())
//                .store_address(store.getAddress())
//                .store_companyNumber(store.getCompanyNumber())
//                .store_createdDate(store.getCreatedDate())
//                .build();
//        return ResponseEntity.ok(dto);
//    }
////==================================================회원 관리==============================================
//
//    /**
//     * 관리할 회원 리스트 보기
//     * res : 총 회원수, 현재 서비스 이용자 수, 아이디, 이름, 전화번호, 이메일, 가입일, 포인트
//     * 링크 : 가입일순으로보기, 이름순으로보기, 검색, 회원탈퇴, 회원수정, 번호표 취소
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/members")
//    public String manageMembers(Pageable pageable, PagedResourcesAssembler<MemberListDto> assembler) {
//        //회원 리스트
//        AdminMemberManageDto dto = AdminMemberManageDto.builder()
//                .totalMemberCount(adminService.totalMemberCount())
//                .currentUsingServiceCount(adminService.currentUsingServiceCount())
////                .memberList(assembler.toModel(adminService.findMembers(pageable, MemberStatus.VALID)))
//                .build();
//        return "";
//    }
//
//    /**
//     * 회원 탈퇴
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/members/{member_id}")
//    public ResponseEntity deleteMember(@PathVariable("member_id")Long member_id) {
//        adminService.deleteMember(member_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("회원 탈퇴 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 회원 정보 보기
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/members/{member_id}")
//    public ResponseEntity updateMember(@PathVariable("member_id")Long member_id) {
//        Member member = adminService.findMember(member_id);
//        AdminMemberDto dto = AdminMemberDto.builder()
//                .member_id(member.getId())
//                .username(member.getUsername())
//                .name(member.getName())
//                .phoneNum(member.getPhoneNum())
//                .email(member.getEmail())
//                .point(member.getPoint())
//                .createdDate(member.getCreatedDate())
//                .build();
//
//
//        return ResponseEntity.ok(dto);
//    }
//
//
//    /**
//     * 회원 수정
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/members/{member_id}")
//    public ResponseEntity updateMember(@PathVariable("member_id")Long member_id, @RequestBody @Valid AdminUpdateMemberForm form) {
//        adminService.updateMember(member_id, form);
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("회원 수정 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//
//    /**
//     * 비밀번호 수정
//     */
//
//    /**
//     * 탈퇴후 7일지난 회원 영구삭제
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/members/auto")
//    public ResponseEntity deleteMembersWeekPast() {
//        adminService.deleteMemberWeekPast();
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("탈퇴회원 삭제 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 번호표 취소
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/members/tickets/{ticket_id}")
//    public ResponseEntity manageMembers(@PathVariable("ticket_id")Long ticket_id, Pageable pageable, PagedResourcesAssembler<MemberListDto> assembler) {
//        //회원 번호표 취소
//        adminService.cancelTicket(ticket_id);
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
////===========================================가입 승인===================================================
//    /**
//     * 가입 승인 가게 목록
//     * 링크 : 가게 관리자 정보, 가입 승인, 이름으로 검색, 주소로 검색
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/wait")
//    public String findStoresWaitingToJoin(Pageable pageable, PagedResourcesAssembler<StoreListDto> assembler) {
//        int totalEnrollStoreCount = adminService.totalEnrollStoreCount();
//        AdminStoreManageDto dto = AdminStoreManageDto.builder()
////                .storeList(assembler.toModel(adminService.findStores(StoreStatus.INVALID, pageable)))
//                .totalEnrollStoreCount(totalEnrollStoreCount)
//                .build();
//        //hateoas
//        return "";
//    }
//
//
//    /**
//     * 가입 승인
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/authorization/{member_id}")
//    public ResponseEntity permitStore(@PathVariable("store_id") Long store_id, @PathVariable("member_id") Long member_id) {
//        adminService.permitStoreAdmin(member_id, store_id);
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가입 승인 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 가입 승인 취소
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @DeleteMapping("/stores/{store_id}/authorization/{member_id}")
//    public ResponseEntity rejectStore(@PathVariable("store_id") Long store_id, @PathVariable("member_id") Long member_id) {
//        adminService.rejectStoreAdmin(member_id, store_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 승인 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
////=============================================시스템 에러=============================================
//    /**
//     * 오류 접수
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PostMapping("/stores/{store_id}/errors")
//    public ResponseEntity sendErrorSystem(@PathVariable("store_id")Long store_id) {
//        adminService.sendErrorSystem(store_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("오류접수 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 시스템 장애 복구 완료
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @PutMapping("/stores/{store_id}/errors")
//    public ResponseEntity errorSystemComplete(@PathVariable("store_id") Long store_id) {
//        adminService.completeErrorSystem(store_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("오류 해결 완료")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 시스템 장애 목록
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/errors")
//    public String errorSystemList(PagedResourcesAssembler<StoreErrorListDto> assembler, Pageable pageable) {
//        return "";
//    }
//
//    /**
//     * 시스템 장애 목록 대기인원순
//     */
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/stores/errors/sequence")
//    public String errorSystemListByWaiting(PagedResourcesAssembler<StoreErrorListDto> assembler, Pageable pageable) {
//        return "";
//    }


}
