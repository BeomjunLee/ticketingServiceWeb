package com.hoseo.hackathon.storeticketingservice.service;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.TicketStatus;
import com.hoseo.hackathon.storeticketingservice.exception.DuplicateTicketingException;
import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
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
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));

        if (ticketRepository.countByMemberUsername(member.getUsername()) > 0) { //번호표 중복 뽑기 체크
            throw new DuplicateTicketingException("이미 번호표를 가지고 있습니다");
        }

        Store store = storeRepository.findByName(storeName).orElseThrow(() -> new NoSuchElementException(storeName + "에 해당되는 가게를 찾을수 없습니다"));
        int totalWaitingCount = ticketRepository.countByStatusEqualsAndStore_Id(TicketStatus.VALID, store.getId());
        
        //티켓 세팅
        ticket.changeTicket(ticket.getPeopleCount(),                                       //인원수(Controller)
                totalWaitingCount + 1,                                          //대기번호
                store.getAvgWaitingTimeByOne() * (totalWaitingCount + 1),       //대기시간
                LocalDateTime.now(),                                                      //발급시간
                TicketStatus.VALID,                                                       //번호표 상태
                ticket.getNotice());                                                      //알림창(Controller)
        ticket.setStore(store); member.setTicket(ticket);   //연관관계 세팅

        store.changeStoreByTicketing(totalWaitingCount);   //Store 갱신
        return ticketRepository.save(ticket);
    }
    
    /**
     * 번호표 취소
     */
    public Long cancelTicket(String storeName, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username + "에 해당되는 유저를 찾을수 없습니다"));
        Store store = storeRepository.findByName(storeName).orElseThrow(() -> new NoSuchElementException(storeName + "에 해당되는 가게를 찾을수 없습니다"));
        Ticket ticket = ticketRepository.findByStore_IdAndMember_Id(store.getId(), member.getId()).orElseThrow(() -> new NoSuchElementException("삭제할 티켓을 찾을수 없습니다"));

        int totalWaitingCount = ticketRepository.countByStatusEqualsAndStore_Id(TicketStatus.VALID, store.getId());

        ticketRepository.updateTicketsMinus1(TicketStatus.VALID, ticket.getWaitingNum(), store.getAvgWaitingTimeByOne(), store.getId()); //취소한 번호표보다 뒤에있는 사람들에게 - 1
        ticket.changeStatusTicket(TicketStatus.CANCEL); //번호표 상태 변경
        store.changeStoreByCancelOrNext(totalWaitingCount); //Store 갱신

        return ticket.getId();
    }

    /**
     * 관리자가 번호표 넘기기
     */

}
