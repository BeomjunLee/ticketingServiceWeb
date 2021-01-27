package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.HoldingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.AvgTimeForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.StoreNoticeForm;
import com.hoseo.hackathon.storeticketingservice.domain.form.TicketForm;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/store")
@Slf4j // 로깅을 위한 어노테이션
public class StoreController {

    private final StoreService storeService;

//===========================================번호표 뽑기========================================
    /**
     * [회원]가게 번호표 뽑기
     * req : 인원수
     */
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{store_id}/createTicket")
    public String createTicket(@PathVariable("store_id") Long store_id, @Valid TicketForm ticketForm, Principal principal, Model model) {
        Ticket ticket = Ticket.builder()
                .peopleCount(ticketForm.getPeopleCount())
                .build();

        storeService.createTicket(ticket, store_id, principal.getName());

        model.addAttribute("message", "번호표가 발급되었습니다");
        return "";
    }

    //===========================================가게 번호표 관리 메뉴========================================
    /**
     * [관리자] 대기중인 회원 리스트 관리 + 현재 가게 정보 관리
     * 응답 : 대기중인 회원정보, 가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
     * link : self, 대기 보류 취소 체크, 가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/manageStore")
    public String manageMembersAndStore(@RequestParam(value = "page", defaultValue = "0") int page, Principal principal, Model model) {
        Store store = storeService.findValidStore(principal.getName());

        Pageable pageable = PageRequest.of(page, 7);

        StoreManageDto dto = StoreManageDto.builder()
                .waitingMembers(storeService.findWaitingMembers(principal.getName(), pageable))
                .storeTicketStatus(store.getStoreTicketStatus())
                .totalWaitingCount(store.getTotalWaitingCount())
                .totalWaitingTime(store.getTotalWaitingTime())
                .notice(store.getNotice())
                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
                //TODO 보류회원 결정나면 작업
                .build();
        model.addAttribute("manageStore", dto);
        return "/store/manageStore";
    }

    /**
     * [관리자] 보류된 회원 리스트 관리
     * 응답 ; 이름, 전화번호
     * link : 보류 ticket별 취소, 체크,
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/holdTickets")
    public String manageHoldMembers(@RequestParam(value = "page", defaultValue = "0") int page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page, 5);
        //보류회원정보
        Page<HoldingMembersDto> holdingMembers = storeService.findHoldMembers(principal.getName(), pageable);
        model.addAttribute("holdingMembers", holdingMembers);
        return "";
    }


    /**
     * [관리자]체크하기
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/checkTicket/{ticket_id}")
    public String checkTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal, Model model) {
        storeService.checkTicket(principal.getName(), ticket_id);
        model.addAttribute("message", "체크되었습니다");
        return "/store/manageStore";
    }

    /**
     * [관리자]취소하기
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/cancelTicket/{ticket_id}")
    public String cancelTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal, Model model) {
        storeService.cancelTicketByAdmin(principal.getName(), ticket_id);
        model.addAttribute("message", "취소되었습니다");
        return "/store/manageStore";
    }

    /**
     * [관리자]보류하기
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/holdTicket/{ticket_id}")
    public String holdTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal, Model model) {
        storeService.holdTicket(principal.getName(), ticket_id);
        model.addAttribute("message", "보류되었습니다");
        return "/store/manageStore";
    }

    /**
     * [관리자] 보류회원 체크하기
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/checkHoldTicket/{ticket_id}")
    public String holdCheckTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal, Model model) {
        storeService.holdCheckTicket(principal.getName(), ticket_id);
        model.addAttribute("message", "체크되었습니다");
        return "/store/manageStore";
    }

    /**
     * [관리자] 보류회원 취소하기
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/cancelHoldTicket/{ticket_id}")
    public String holdCancelTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal, Model model) {
        storeService.holdCancelTicket(principal.getName(), ticket_id);
        model.addAttribute("message", "취소되었습니다");
        return "/store/manageStore";
    }

    /**
     * 가게 번호표 활성화
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/openStore")
    public String openTicket(Principal principal, Model model) {
        storeService.openTicket(principal.getName());
        model.addAttribute("message", "번호표가 활성화되었습니다");
        return "/store/manageStore";
    }

    /**
     * 가게 번호표 비활성화
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/closeStore")
    public String closeTicket(Principal principal, Model model) {
        storeService.closeTicket(principal.getName());
        model.addAttribute("message", "번호표가 활성화되었습니다");
        return "/store/manageStore";
    }

    /**
     * 오류 신청
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/applyError")
    public String sendErrorSystem(Principal principal, Model model) {
        storeService.sendErrorSystem(principal.getName());
        model.addAttribute("message", "오류가 접수되었습니다");
        return "/store/manageStore";
    }

    /**
     * 공지사항 수정
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/updateNotice")
    public String updateNotice(Principal principal, @RequestBody @Valid StoreNoticeForm form, Model model) {
        storeService.updateStoreNotice(principal.getName(), form.getNotice());
        model.addAttribute("message", "수정되었습니다");
        return "/store/manageStore";
    }

    /**
     * 한사람당 대기시간 수정
     */
    @PreAuthorize("hasAuthority('STORE_ADMIN')")
    @GetMapping("/updateTime")
    public String updateAvgWaitingTime(Principal principal, @RequestBody @Valid AvgTimeForm form, Model model) {
        storeService.updateAvgTime(principal.getName(), form.getAvgWaitingTimeByOne());
        model.addAttribute("message", "수정되었습니다");
        return "/store/manageStore";
    }

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
