package com.hoseo.hackathon.storeticketingservice.service;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.TicketStatus;
import com.hoseo.hackathon.storeticketingservice.exception.DuplicateTicketingException;
import com.hoseo.hackathon.storeticketingservice.exception.NoFindMyTicketException;
import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService {
    private final MemberRepository memberRepository;
    private final TicketRepository ticketRepository;
    private final StoreRepository storeRepository;

    /**
     * 번호표 뽑기
     */
    public Ticket createTicket(Ticket ticket, String storeName, String username) {
        //회원 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));

        if (ticketRepository.countByMemberUsername(member.getUsername()) > 0) { //번호표 중복 뽑기 체크
            throw new DuplicateTicketingException("이미 번호표를 가지고 있습니다");
        }
        
        //가게 이름으로 가게 찾기
        Store store = storeRepository.findByName(storeName).orElseThrow(() -> new NoSuchElementException(storeName + "에 해당되는 가게를 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);


        //티켓 세팅
        ticket.changeTicket(ticket.getPeopleCount(),                                       //인원수(Controller)
                totalWaitingCount + 1,                                          //대기번호
                store.getAvgWaitingTimeByOne() * (totalWaitingCount + 1),       //대기시간
                LocalDateTime.now(),                                                      //발급시간
                TicketStatus.VALID,                                                       //번호표 상태
                ticket.getNotice());                                                      //알림창(Controller)
        ticket.setStore(store); ticket.setMember(member);   //연관관계 세팅

        store.changeStoreByTicketing(totalWaitingCount);   //Store 갱신
        return ticketRepository.save(ticket);
    }
    
    /**
     * 번호표 취소
     */
    public Ticket cancelTicket(String storeName, String username) {
        //회원 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        //가게이름으로 가게찾기
        Store store = storeRepository.findByName(storeName).orElseThrow(() -> new NoSuchElementException(storeName + "에 해당되는 가게를 찾을수 없습니다"));
        //회원이 발행한 번호표 찾기
        Ticket ticket = ticketRepository.findById(member.getTicket().getId()).orElseThrow(() -> new NoSuchElementException("삭제할 티켓을 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //취소한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.CANCEL); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }

    /**
     * 관리자가 번호표 넘기기(체크)
     */
    public Ticket checkTicket(String username) {
        //관리자 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        //관리자의 가게 찾기
        Store store = storeRepository.findByMember_Id(member.getId())
                .orElseThrow(() -> new NoSuchElementException(member.getName() + "라는 관리자의 이름으로 등록된 가게를 찾을수 없습니다"));
        //VALID상태의 첫번째 대기순서 번호표 찾기
        Ticket ticket = ticketRepository.findByStore_IdAndWaitingNumAndStatus(store.getId(), 1, TicketStatus.VALID).orElseThrow(() -> new NoSuchElementException("삭제할 티켓을 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //보류한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.INVALID); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }


    /**
     * 관리자가 번호표 보류하기
     */
    public Ticket holdTicket(String username) {
        //관리자 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        //관리자의 가게 찾기
        Store store = storeRepository.findByMember_Id(member.getId())
                .orElseThrow(() -> new NoSuchElementException(member.getName() + "라는 관리자의 이름으로 등록된 가게를 찾을수 없습니다"));
        //VALID상태의 첫번째 대기순서 번호표 찾기
        Ticket ticket = ticketRepository.findByStore_IdAndWaitingNumAndStatus(store.getId(), 1, TicketStatus.VALID)
                .orElseThrow(() -> new NoSuchElementException("보류할 티켓을 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStore_IdAndStatus(store.getId(), TicketStatus.VALID);

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //보류한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.HOLD); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket;
    }

    /**
     * 관리자가 보류한 번호표 체크
     */
    public Ticket holdCheckTicket(Long ticket_id) {
        Ticket ticket = ticketRepository.findById(ticket_id).orElseThrow(() -> new NoSuchElementException("체크할 티켓을 찾을수 없습니다"));

        ticket.changeStatusTicket(TicketStatus.INVALID);//번호표 상태 변경
        return ticket;
    }

    /**
     * 관리자가 보류한 티켓들 보기
     */
    @Transactional(readOnly = true)
    public Page<Ticket> findHoldTickets(String username, Pageable pageable) {
        //관리자 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));

        //관리자의 가게 찾기
        Store store = storeRepository.findByMember_Id(member.getId())
                .orElseThrow(() -> new NoSuchElementException(member.getName() + "라는 관리자의 이름으로 등록된 가게를 찾을수 없습니다"));

        return ticketRepository.findAllByStore_IdAndStatus(store.getId(), TicketStatus.HOLD, pageable);
    }

    /**
     * [관리자] VALID n번째 대기손님 정보 보기(다음 순서 회원정보)
     */
    @Transactional(readOnly = true)
    public Ticket findValidTicketByWaitingNum(String username, int waitingNum) {
        //관리자 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        //관리자의 가게 찾기
        Store store = storeRepository.findByMember_Id(member.getId())
                .orElseThrow(() -> new NoSuchElementException(member.getName() + "라는 관리자의 이름으로 등록된 가게를 찾을수 없습니다"));
        //티켓 찾기
        return ticketRepository.findByStore_IdAndWaitingNumAndStatus(store.getId(), waitingNum, TicketStatus.VALID)
                .orElseThrow(() -> new NoSuchElementException("대기손님의 티켓을 찾을수 없습니다"));
    }


    /**
     * 회원이 자기 티켓 정보 보기
     */
    @Transactional(readOnly = true)
    public Ticket findMyTicket(String username) {
        //회원 찾기
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        Ticket ticket = member.getTicket();
        if (ticket == null) {
            throw new NoFindMyTicketException(member.getName() + "님의 티켓이 없습니다");
        }
        return ticket;
    }
}
