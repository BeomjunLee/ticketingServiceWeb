package com.hoseo.hackathon.storeticketingservice.controller;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MembersAndTicketsDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreWaitingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.form.TicketForm;
import com.hoseo.hackathon.storeticketingservice.domain.resource.HoldingMembersResource;
import com.hoseo.hackathon.storeticketingservice.domain.resource.StoreWaitingMembersResource;
import com.hoseo.hackathon.storeticketingservice.domain.resource.WaitingMembersResource;
import com.hoseo.hackathon.storeticketingservice.domain.response.Response;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import com.hoseo.hackathon.storeticketingservice.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/stores", produces = MediaTypes.HAL_JSON_VALUE)
public class StoreController {

    private final StoreService storeService;

    /**
     * [회원]가게 번호표 뽑기
     * req : 인원수
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{store_id}/new/tickets")
    public ResponseEntity createTicket(@PathVariable("store_id") Long store_id, @Valid @RequestBody TicketForm ticketForm, Principal principal) {
        Ticket ticket = Ticket.builder()
                .peopleCount(ticketForm.getPeopleCount())
                .build();

        storeService.createTicket(ticket, store_id, principal.getName());

        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("번호표 발급 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * [관리자] 대기중인 회원 리스트보기
     * res: 대기중인 회원정보, 보류된 회원정보, 가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
     * link : self, 대기 보류 취소 체크, 보류 ticket별 취소, 체크, 가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @GetMapping("/tickets")
    public ResponseEntity ticketList(Principal principal, Pageable pageable, PagedResourcesAssembler<MembersAndTicketsDto> assembler) {
        Store store = storeService.findStore(principal.getName());
        StoreWaitingMembersDto dto = StoreWaitingMembersDto.builder()
                .waitingMembers(assembler.toModel(storeService.findWaitingMembers(principal.getName(), pageable), e -> new WaitingMembersResource(e)))
                .holdingMembers(assembler.toModel(storeService.findHoldMembers(principal.getName(), pageable), e -> new HoldingMembersResource(e)))
                .storeTicketStatus(store.getStoreTicketStatus())
                .totalWaitingCount(store.getTotalWaitingCount())
                .totalWaitingTime(store.getTotalWaitingTime())
                .notice(store.getNotice())
                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
                .build();
        //hateoas
        StoreWaitingMembersResource resource = new StoreWaitingMembersResource(dto);
        return ResponseEntity.ok(resource);
    }

    /**
     * [관리자]체크하기
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @PostMapping("/tickets/{ticket_id}")
    public ResponseEntity checkTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
        storeService.checkTicket(principal.getName(), ticket_id);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("번호표 체크 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * [관리자]취소하기
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @DeleteMapping("/tickets/{ticket_id}")
    public ResponseEntity cancelTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
        storeService.cancelTicketByAdmin(principal.getName(), ticket_id);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("번호표 취소 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * [관리자]보류하기
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @GetMapping("/tickets/{ticket_id}")
    public ResponseEntity holdTicket(@PathVariable("ticket_id")Long ticket_id, Principal principal) {
        storeService.holdTicket(principal.getName(), ticket_id);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("번호표 보류 성공")
                .build();
        return ResponseEntity.ok(response);
    }
    
    /**
     * [관리자] 보류회원 체크하기
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @PostMapping("/tickets/{ticket_id}/holding")
    public ResponseEntity holdCheckTicket(@PathVariable("ticket_id")Long ticket_id) {
        storeService.holdCheckTicket(ticket_id);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("보류 번호표 체크 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * [관리자] 보류회원 취소하기
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @DeleteMapping("/tickets/{ticket_id}/holding")
    public ResponseEntity holdCancelTicket(@PathVariable("ticket_id")Long ticket_id) {
        storeService.holdCancelTicket(ticket_id);
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("보류 번호표 취소 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 가게 번호표 활성화
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @PostMapping("/status")
    public ResponseEntity openTicket(Principal principal) {
        storeService.openTicket(principal.getName());
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("가게 번호표 활성화 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 가게 번호표 비활성화
     */
    @PreAuthorize("hasRole('ROLE_STORE_ADMIN')")
    @GetMapping("/status")
    public ResponseEntity closeTicket(Principal principal) {
        storeService.closeTicket(principal.getName());
        Response response = Response.builder()
                .result("success")
                .status(200)
                .message("가게 번호표 비활성화 성공")
                .build();
        return ResponseEntity.ok(response);
    }

    /**
     * 공지사항 수정
     */

    /**
     * 한사람당 대기시간 수정
     */


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
