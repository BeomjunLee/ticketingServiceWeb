package com.hoseo.hackathon.storeticketingservice.service;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.TicketStatus;
import com.hoseo.hackathon.storeticketingservice.exception.DuplicateTicketingException;
import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.repository.TicketRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TicketServiceTest {

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    MemberRepository memberRepository;
    
    @Test
    @Rollback(value = false)
    @Transactional
    @Order(5)
    public void 티켓취소_테스트() throws Exception{ //1. 티켓 취소했을때 Ticket의 상태가 바뀌어야 하고
                                                 // 2. 취소한 티켓의 번호보다 뒤에있는 행들이 앞당겨져야하고
                                                 // 3. Store에 데이터들도 바뀌어야한다
        //given
        //when
        Long ticket_id = ticketService.cancelTicket("식당1", "test");
        Member member = memberRepository.findByUsername("admin").get();
        Store findStore = storeRepository.findByName("식당1").get();
        Ticket findTicket = ticketRepository.findByStore_IdAndMember_Id(findStore.getId(), member.getId()).get();

        Ticket ticket = ticketRepository.findById(ticket_id).get();
        Ticket ticket2 = ticketRepository.findById(findTicket.getId()).get();
        Store store = storeRepository.findByName("식당1").get();
        
        //then
        Assertions.assertEquals(TicketStatus.CANCEL, ticket.getStatus());   //티켓 상태 확인
        Assertions.assertEquals(1, ticket2.getWaitingNum());         //번호표가 앞당겨졌는지
        Assertions.assertEquals(5, ticket2.getWaitingTime());         //대기시간이 앞당겨졌는지
        Assertions.assertEquals(3, store.getTotalWaitingCount());  //전체 대기인원수가 줄었는지
        Assertions.assertEquals(15, store.getTotalWaitingTime());  //전체 대기시간이 줄었는지

    }
    
    @Test
    @Transactional
    @Order(4)
    public void 가게테이블_변경확인_테스트() throws Exception{
        //given
        //when
        Store store = storeRepository.findByName("식당1").get();
        //then
        Assertions.assertEquals(4, store.getTotalWaitingCount());   //전체 대기인원수가 느는지
        Assertions.assertEquals(20, store.getTotalWaitingTime());   //전체 대기시간이 느는지
    }
    @Test
    @Transactional
    @Order(3)
    public void 같은회원_중복뽑기_불가능_테스트() throws Exception{
        //given
        //when
        Ticket ticket = Ticket.builder()
                .peopleCount(3)
                .build();
        //then
        Assertions.assertThrows(DuplicateTicketingException.class, () ->{
            ticketService.createTicket(ticket, "식당1", "test");
        });
    }
    
    @Test
    @Rollback(value = false)
    @Transactional
    @Order(2)
    public void 번호표_발급_테스트2() throws Exception{
        //given
        //when
        Ticket ticket = Ticket.builder()
                .peopleCount(4)
                .build();
        Ticket ticket2 = Ticket.builder()
                .peopleCount(3)
                .build();
        Ticket ticket3 = Ticket.builder()
                .peopleCount(3)
                .build();

        Ticket savedTicket = ticketService.createTicket(ticket, "식당1", "admin");
        Ticket savedTicket2 = ticketService.createTicket(ticket2, "식당1", "test2");
        Ticket savedTicket3 = ticketService.createTicket(ticket3, "식당1", "test3");
        
        //then
        Assertions.assertEquals(2, savedTicket.getWaitingNum()); //번호표가 느는지 체크
        Assertions.assertEquals(10, savedTicket.getWaitingTime()); //번호표가 느는지 체크
    }

    @Test
    @Rollback(value = false)
    @Transactional
    @Order(1)
    public void 번호표_발급_테스트1() throws Exception{
        //given
        //when
        Ticket ticket = Ticket.builder()
                .peopleCount(1)
                .build();

        Ticket savedTicket = ticketService.createTicket(ticket, "식당1", "test");

        //then
        Assertions.assertEquals(1, savedTicket.getWaitingNum()); //번호표가 느는지 체크
        Assertions.assertEquals(5, savedTicket.getWaitingTime()); //번호표가 느는지 체크
    }

}