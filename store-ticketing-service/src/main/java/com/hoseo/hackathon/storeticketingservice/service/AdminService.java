package com.hoseo.hackathon.storeticketingservice.service;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.dto.HoldingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberListDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.WaitingMembersDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreListDto;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreStatus;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreTicketStatus;
import com.hoseo.hackathon.storeticketingservice.domain.status.TicketStatus;
import com.hoseo.hackathon.storeticketingservice.exception.IsAlreadyCompleteException;
import com.hoseo.hackathon.storeticketingservice.exception.NotAuthorizedStoreException;
import com.hoseo.hackathon.storeticketingservice.exception.NotFoundStoreException;
import com.hoseo.hackathon.storeticketingservice.exception.NotFoundTicketException;
import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final StoreRepository storeRepository;

//===============================================가게 관리=============================================

    /**
     * 가게 리스트 보기
     */
    public Page<StoreListDto> findStores(StoreStatus storeStatus, Pageable pageable) {
        Page<Store> stores = storeRepository.findAllByStoreStatus(storeStatus, pageable);
        return stores.map(store -> StoreListDto.builder()
                .store_id(store.getId())
                .member_id(store.getMember().getId())
                .name(store.getName())
                .phoneNum(store.getPhoneNum())
                .address(store.getAddress())
                .createdDate(store.getCreatedDate())
                .companyNumber(store.getCompanyNumber())
                .build());
    }

    /**
     * 등록된 가게 수 보기
     */
    public int totalEnrollStoreCount() {
        return storeRepository.countByStoreStatus(StoreStatus.VALID);
    }

    /**
     * 가게 관리자 정보 보기
     */
    public Member findStoreAdmin(Long member_id) {
        return memberRepository.findById(member_id).orElseThrow(() -> new NoSuchElementException("가게 관리자를 찾을수 없습니다"));
    }

    /**
     * 가게 수정
     */

    /**
     * 가게 이름으로 검색
     */

    /**
     * 가게 주소로 검색
     */

                   //================가게 번호표 관리 기능================
    /**
     * [관리자] 번호표 발급 활성화
     */
    @Transactional
    public void openTicket(Long store_id) {
        Store store = storeRepository.findById(store_id)
                .orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));
        if (store.getStoreTicketStatus().equals(StoreTicketStatus.OPEN)) {
            throw new IsAlreadyCompleteException("이미 활성화 되어있습니다");
        }
        store.changeStoreTicketStatus(StoreTicketStatus.OPEN);
    }

    /**
     * [관리자] 번호표 발급 비활성화
     */
    @Transactional
    public void closeTicket(Long store_id) {
        Store store = storeRepository.findById(store_id)
                .orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));
        if (store.getStoreTicketStatus().equals(StoreTicketStatus.CLOSE)) {
            throw new IsAlreadyCompleteException("이미 비활성화 되어있습니다");
        }
        store.changeStoreTicketStatus(StoreTicketStatus.CLOSE);
    }

    /**
     * [관리자]번호표 취소
     */
    @Transactional
    public Ticket cancelTicketByAdmin(Long store_id, Long ticket_id) {

        Store store = storeRepository.findById(store_id)
                .orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));

        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NotFoundTicketException("티켓을 찾을수 없습니다"));

        if (ticket.getStatus().equals(TicketStatus.CANCEL)) {
            throw new IsAlreadyCompleteException("이미 취소처리 되었습니다");
        }

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //취소한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.CANCEL); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }

    /**
     * [회원]번호표 취소
     */
    @Transactional
    public Ticket cancelTicket(Long ticket_id) {

        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NotFoundTicketException("티켓을 찾을수 없습니다"));
        if (ticket.getStatus().equals(TicketStatus.CANCEL)) {
            throw new IsAlreadyCompleteException("이미 취소처리 되었습니다");
        }

        Store store = storeRepository.findById(ticket.getStore().getId()).orElseThrow(() -> new NotFoundStoreException("해당되는 가게를 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //취소한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.CANCEL); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }

    /**
     * [관리자] 번호표 넘기기(체크)
     */
    @Transactional
    public Ticket checkTicket(Long store_id, Long ticket_id) {
        //번호표 찾기
        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NotFoundTicketException("체크할 티켓을 찾을수 없습니다"));
        if (ticket.getStatus().equals(TicketStatus.INVALID)) {
            throw new IsAlreadyCompleteException("이미 체크처리 되었습니다");
        }

        //관리자의 가게 찾기
        Store store = storeRepository.findById(store_id).orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //보류한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.INVALID); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }


    /**
     * [관리자] 번호표 보류하기
     */
    @Transactional
    public Ticket holdTicket(Long store_id, Long ticket_id) {
        //번호표 찾기
        Ticket ticket = ticketRepository.findById(ticket_id)
                .orElseThrow(() -> new NotFoundTicketException("보류할 티켓을 찾을수 없습니다"));
        if (ticket.getStatus().equals(TicketStatus.HOLD)) {
            throw new IsAlreadyCompleteException("이미 보류처리 되었습니다");
        }
        //관리자의 가게 찾기
        Store store = storeRepository.findById(store_id).orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //보류한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.HOLD); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }

    /**
     * [관리자]보류한 번호표 체크
     */
    @Transactional
    public Ticket holdCheckTicket(Long ticket_id) {
        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NotFoundTicketException("체크할 티켓을 찾을수 없습니다"));
        if (ticket.getStatus().equals(TicketStatus.INVALID)) {
            throw new IsAlreadyCompleteException("이미 체크처리 되었습니다");
        }

        ticket.changeStatusTicket(TicketStatus.INVALID);//번호표 상태 변경
        return ticket;
    }

    /**
     * [관리자] 보류한 번호표 취소
     */
    @Transactional
    public Ticket holdCancelTicket(Long ticket_id) {
        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NotFoundTicketException("체크할 티켓을 찾을수 없습니다"));
        if (ticket.getStatus().equals(TicketStatus.CANCEL)) {
            throw new IsAlreadyCompleteException("이미 취소처리 되었습니다");
        }

        ticket.changeStatusTicket(TicketStatus.CANCEL);//번호표 상태 변경
        return ticket;
    }

    /**
     * [관리자] 보류한 번호표들 보기
     */
    public Page<HoldingMembersDto> findHoldMembers(Long store_id, Pageable pageable) {
        //관리자의 가게 찾기
        Store store = storeRepository.findById(store_id).orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));

        if (store.getStoreStatus().equals(StoreStatus.INVALID)) {   //가게 승인여부 체크
            throw new NotAuthorizedStoreException("승인되지 않은 가게입니다");
        }

        Page<Ticket> tickets = ticketRepository.findAllByStore_IdAndStatus(store.getId(), TicketStatus.HOLD, pageable);
        return tickets.map(ticket -> HoldingMembersDto.builder()
                .name(ticket.getMember().getName())
                .phoneNum(ticket.getMember().getPhoneNum())
                .ticket_id(ticket.getId())
                .build());
    }

    /**
     * [관리자] 대기중인 회원들 보기
     */
    public Page<WaitingMembersDto> findWaitingMembers(Long store_id, Pageable pageable) {
        //관리자의 가게 찾기
        Store store = storeRepository.findById(store_id).orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));

        Page<Ticket> tickets = ticketRepository.findAllByStore_IdAndStatus(store.getId(), TicketStatus.VALID, pageable);
        return tickets.map(ticket -> WaitingMembersDto.builder()
                .waitingNum(ticket.getWaitingNum())
                .name(ticket.getMember().getName())
                .phoneNum(ticket.getMember().getPhoneNum())
                .ticket_id(ticket.getId())
                .build());
    }

    /**
     * [관리자] 가게 정보 보기
     */
    public Store findStore(Long store_id) {
        return storeRepository.findById(store_id).orElseThrow(() -> new NotFoundStoreException("등록된 가게를 찾을수 없습니다"));
    }


//==========================================================회원 관리===============================================================

    /**
     * 회원 리스트 보기
     */
    public Page<MemberListDto> findMembers(Pageable pageable) {
        return memberRepository.findAllByUsernameIsNotNull(pageable).map(member -> MemberListDto.builder()
                    .ticket_id(ticketRepository.findTicketIdJoinMemberId(member.getId())
                            .orElseThrow(() -> new NotFoundTicketException("체크할 티켓을 찾을수 없습니다")))
                    .member_id(member.getId())
                    .username(member.getUsername())
                    .name(member.getName())
                    .phoneNum(member.getPhoneNum())
                    .email(member.getEmail())
                    .createdDate(member.getCreatedDate())
                    .build());
    }

    /**
     * 전체 회원 수
     */
    public int totalMemberCount() {
        return memberRepository.countByUsernameIsNotNull();
    }

    /**
     * 현재 서비스 이용자 수(번호표 뽑은)
     */
    public int currentUsingServiceCount() {
        return ticketRepository.countByStatus(TicketStatus.VALID);
    }

    /**
     * 회원 정보 보기
     */

    /**
     * 회원 티켓 취소
     */


//==================================================헤더=========================================================
    /**
     * 가입 승인 대기 수
     */

    /**
     * 시스템 장애 건 수
     */

//============================================가입 승인====================================================
    /**
     * 가입 대기 리스트보기
     */

    /**
     * 가게 관리자 가입 승인 (시간넣어야됨)
     */

    /**
     * 가입 대기 이름 검색
     */

    /**
     * 가입 대기 주소 검색
     */

//==============================================시스템 장애================================
    /**
     * 시스템 장애 리스트 보기
     */

}
