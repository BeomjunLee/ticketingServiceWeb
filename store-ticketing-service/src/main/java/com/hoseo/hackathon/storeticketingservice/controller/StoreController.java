package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.HoldingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.WaitingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.AvgTimeForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.StoreNoticeForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.TicketForm;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreTicketStatus;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/stores")
@Slf4j // 로깅을 위한 어노테이션
public class StoreController {

    private final StoreService storeService;

//===========================================번호표 뽑기========================================
//    /**
//     * [회원]가게 번호표 뽑기
//     * req : 인원수
//     */
//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PostMapping("/{store_id}/new/tickets")
//    public ResponseEntity createTicket(@PathVariable("store_id") Long store_id, @Valid @RequestBody TicketForm ticketForm, Principal principal) {
//        Ticket ticket = Ticket.builder()
//                .peopleCount(ticketForm.getPeopleCount())
//                .build();
//
//        storeService.createTicket(ticket, store_id, principal.getName());
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 발급 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    //===========================================가게 번호표 관리 메뉴========================================
//    /**
//     * [관리자] 대기중인 회원 리스트 관리 + 현재 가게 정보 관리
//     * 응답 : 대기중인 회원정보, 가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
//     * link : self, 대기 보류 취소 체크, 가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @GetMapping("/tickets")
//    public String manageMembersAndStore(Principal principal, Pageable pageable, PagedResourcesAssembler<WaitingMembersDto> assembler) {
//        Store store = storeService.findValidStore(principal.getName());
//
//        StoreManageDto dto = StoreManageDto.builder()
////                .waitingMembers(assembler.toModel(storeService.findWaitingMembers(principal.getName(), pageable)))
//                .storeTicketStatus(store.getStoreTicketStatus())
//                .totalWaitingCount(store.getTotalWaitingCount())
//                .totalWaitingTime(store.getTotalWaitingTime())
//                .notice(store.getNotice())
//                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
//                //TODO 보류회원 결정나면 작업
//                .build();
//        //hateoas
//        return "";
//    }
//
//    /**
//     * [관리자] 보류된 회원 리스트 관리
//     * 응답 ; 이름, 전화번호
//     * link : 보류 ticket별 취소, 체크,
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @GetMapping("/tickets/hold")
//    public String manageHoldMembers(Principal principal, Pageable pageable, PagedResourcesAssembler<HoldingMembersDto> assembler) {
//
//        //보류회원정보
//
//        return "";
//    }
//
//
//    /**
//     * [관리자]체크하기
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PostMapping("/tickets/{ticket_id}")
//    public ResponseEntity checkTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
//        storeService.checkTicket(principal.getName(), ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 체크 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * [관리자]취소하기
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @DeleteMapping("/tickets/{ticket_id}")
//    public ResponseEntity cancelTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
//        storeService.cancelTicketByAdmin(principal.getName(), ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * [관리자]보류하기
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PutMapping("/tickets/{ticket_id}")
//    public ResponseEntity holdTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
//        storeService.holdTicket(principal.getName(), ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("번호표 보류 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * [관리자] 보류회원 체크하기
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PostMapping("/tickets/{ticket_id}/hold")
//    public ResponseEntity holdCheckTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
//        storeService.holdCheckTicket(principal.getName(), ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("보류 번호표 체크 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * [관리자] 보류회원 취소하기
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @DeleteMapping("/tickets/{ticket_id}/hold")
//    public ResponseEntity holdCancelTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
//        storeService.holdCancelTicket(principal.getName(), ticket_id);
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("보류 번호표 취소 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 가게 번호표 활성화
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PostMapping("/status")
//    public ResponseEntity openTicket(Principal principal) {
//        storeService.openTicket(principal.getName());
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 번호표 활성화 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 가게 번호표 비활성화
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PutMapping("/status")
//    public ResponseEntity closeTicket(Principal principal) {
//        storeService.closeTicket(principal.getName());
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("가게 번호표 비활성화 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 오류 신청
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PostMapping("/errors")
//    public ResponseEntity sendErrorSystem(Principal principal) {
//        storeService.sendErrorSystem(principal.getName());
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("오류접수 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }
//
//    /**
//     * 공지사항 수정
//     */
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PutMapping("/notice")
//    public ResponseEntity updateNotice(Principal principal, @RequestBody @Valid StoreNoticeForm form) {
//        storeService.updateStoreNotice(principal.getName(), form.getNotice());
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
//    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
//    @PutMapping("/time")
//    public ResponseEntity updateAvgWaitingTime(Principal principal, @RequestBody @Valid AvgTimeForm form) {
//        storeService.updateAvgTime(principal.getName(), form.getAvgWaitingTimeByOne());
//
//        Response response = Response.builder()
//                .result("success")
//                .status(200)
//                .message("대기시간 변경 성공")
//                .build();
//        return ResponseEntity.ok(response);
//    }

    //카카오맵 api 와 DB연동 테스트
    @GetMapping("/searchStore")
    public String searchStore(Model model)
    {
        List<Store> storeList = new ArrayList<>();

        //테스트 DB
        storeList.add(new Store(Long.getLong("1"),"test","010-0000-0000","testAddress","36.7915156728683","127.130352628969",0,0,0,
                "test","111111111",null,null,null,null,null));

        //StoreRepository.findAll().forEach(e -> storeList.add(e));

        //model.addAttribute("id_num", storeList.size());

        log.info("가게 데이터 개수 : " + String.valueOf(storeList.size()));
        model.addAttribute("stores", storeList);

        return "searchStore";
    }

    /**
     * 가게 수정
     */

//===========================================가게 찾기 메뉴========================================

    /**
     * 가게 보기
     */


    /**
     * 가게 상세보기
     */

    /**
     * 가게이름으로 검색
     */

}
