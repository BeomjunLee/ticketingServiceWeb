//package com.hoseo.hackathon.storeticketingservice.service;
//
//import com.hoseo.hackathon.storeticketingservice.domain.Member;
//import com.hoseo.hackathon.storeticketingservice.domain.Store;
//import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
//import com.hoseo.hackathon.storeticketingservice.domain.status.TicketStatus;
//import com.hoseo.hackathon.storeticketingservice.domain.dto.WaitingMembersDto;
//import com.hoseo.hackathon.storeticketingservice.exception.DuplicateTicketingException;
//import com.hoseo.hackathon.storeticketingservice.exception.NotFoundTicketException;
//import com.hoseo.hackathon.storeticketingservice.repository.MemberRepository;
//import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
//import com.hoseo.hackathon.storeticketingservice.repository.TicketRepository;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.annotation.Rollback;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class StoreServiceTest {
//
//    @Autowired
//    StoreService storeService;
//
//    @Autowired
//    TicketRepository ticketRepository;
//
//    @Autowired
//    StoreRepository storeRepository;
//
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Order(10)
//    public void 회원_티켓_찾기_없을경우() throws Exception{
//        //given
//        //when
//        //then
//        assertThrows(NotFoundTicketException.class, () -> {
//            storeService.findMyTicket("test5");
//        });
//    }
////    @Test
////    @Order(9)
////    public void 관리자가_보류한_티켓_찾기() throws Exception{
////        //given
////        //when
////        Pageable pageable = null;
////        Page<WaitingMembersDto> ticket = storeService.findHoldMembers("admin", pageable);
////        //then
////        assertEquals(1, ticket.getTotalPages());
////        assertEquals(1, ticket.getContent().get(0).getWaitingNum());   //대기번호
////        assertEquals("회원1", ticket.getContent().get(0).getName());  //이름
////
////    }
//
//    @Test
//    @Order(8)
//    public void 회원_티켓_찾기() throws Exception{
//        //given
//        Ticket ticket = storeService.findMyTicket("test");
//        //then
//        assertEquals(1, ticket.getWaitingNum());
//        assertEquals(5, ticket.getWaitingTime());
//    }
//
//    @Test
//    @Rollback(value = false)
//    @Order(7)
//    public void 티켓체크_테스트() throws Exception{ //1. 티켓 체크했을때 Ticket의 상태가 바뀌어야 하고
//        // 2. 체크한 티켓의 번호보다 뒤에있는 행들이 앞당겨져야하고
//        // 3. Store에 데이터들도 바뀌어야한다
//        //given
//        //when
//        Member member = memberRepository.findByUsername("admin").get(); //관리자
//        Ticket ticket = ticketRepository.findTicketJoinMemberByUsernameAndStatus("test2", TicketStatus.VALID).get();
//        Ticket checkTicket = storeService.checkTicket(member.getUsername(), ticket.getId());   //티켓 체크
//
//        Store findStore = storeRepository.findByName("식당1").get();  //식당1의 식당정보 찾기
//        Ticket findTicket = ticketRepository.findById(checkTicket.getId()).get();    //체크한 티켓정보
//
//        Ticket ticket2 = ticketRepository.findTicketJoinMemberByUsernameAndStatus("test4", TicketStatus.VALID).get();
//        Ticket findNextTicket = ticketRepository.findById(ticket2.getId()).get(); //체크한 다음 순서 사람의 티켓정보
//
//        //then
//        assertEquals(TicketStatus.INVALID, findTicket.getStatus());   //티켓 상태 확인(VALID -> INVALID)
//
//        assertEquals(1, findNextTicket.getWaitingNum());    //다음 사람의 번호표가 앞당겨졌는지
//        assertEquals(5, findNextTicket.getWaitingTime());   //다음 사람의 대기시간이 앞당겨졌는지
//        assertEquals(2, findStore.getTotalWaitingCount());  //전체 대기인원수가 줄었는지
//        assertEquals(10, findStore.getTotalWaitingTime());  //전체 대기시간이 줄었는지
//
//    }
//
//    @Test
//    @Rollback(value = false)
//    @Order(6)
//    public void 티켓보류_테스트() throws Exception{ //1. 티켓 취소했을때 Ticket의 상태가 바뀌어야 하고
//        // 2. 보류한 티켓의 번호보다 뒤에있는 행들이 앞당겨져야하고
//        // 3. Store에 데이터들도 바뀌어야한다
//        //given
//        //when
//        Member member = memberRepository.findByUsername("admin").get(); //관리자
//        Ticket ticket = ticketRepository.findTicketJoinMemberByUsernameAndStatus("test1", TicketStatus.VALID).get();
//        Ticket holdTicket = storeService.holdTicket(member.getUsername(), ticket.getId());   //티켓 보류(무조건 1번만 보류됨)
//
//        Store findStore = storeRepository.findByName("식당1").get();  //식당1의 식당정보 찾기
//        Ticket findTicket = ticketRepository.findById(holdTicket.getId()).get();    //보류한 티켓정보
//
//        Ticket ticket2 = ticketRepository.findTicketJoinMemberByUsernameAndStatus("test2", TicketStatus.VALID).get();
//        Ticket findNextTicket = ticketRepository.findById(ticket2.getId()).get(); //보류한 다음 순서 사람의 티켓정보
//
//        //then
//        assertEquals(TicketStatus.HOLD, findTicket.getStatus());   //티켓 상태 확인(VALID -> HOLD)
//
//        assertEquals(1, findNextTicket.getWaitingNum());    //다음 사람의 번호표가 앞당겨졌는지
//        assertEquals(5, findNextTicket.getWaitingTime());   //다음 사람의 대기시간이 앞당겨졌는지
//        assertEquals(3, findStore.getTotalWaitingCount());  //전체 대기인원수가 줄었는지
//        assertEquals(15, findStore.getTotalWaitingTime());  //전체 대기시간이 줄었는지
//
//    }
//
////    @Test
////    @Rollback(value = false)
////    @Order(5)
////    public void 티켓취소_테스트() throws Exception{ //1. 티켓 취소했을때 Ticket의 상태가 바뀌어야 하고
////                                                 // 2. 취소한 티켓의 번호보다 뒤에있는 행들이 앞당겨져야하고
////                                                 // 3. Store에 데이터들도 바뀌어야한다
////        //given
////        //when
////        Store findStore = storeRepository.findStoreJoinMemberByUsername("admin").get();
////        Ticket cancelTicket = storeService.cancelTicket("test3");   //test3취소
////
////        Member member = memberRepository.findByUsername("test4").get();
////
////        Ticket ticket = ticketRepository.findById(cancelTicket.getId()).get();  //취소한 티켓정보
////        Ticket findTicket = ticketRepository.findById(member.getTicket().getId()).get();    //test4의 티켓
////        Store store = storeRepository.findByName("식당1").get();
////
////        //then
////        assertEquals(TicketStatus.CANCEL, ticket.getStatus());   //티켓 상태 확인
////        assertEquals(3, findTicket.getWaitingNum());         //번호표가 앞당겨졌는지
////        assertEquals(15, findTicket.getWaitingTime());         //대기시간이 앞당겨졌는지
////        assertEquals(4, store.getTotalWaitingCount());  //전체 대기인원수가 줄었는지
////        assertEquals(20, store.getTotalWaitingTime());  //전체 대기시간이 줄었는지
////
////    }
//
//    @Test
//    @Order(4)
//    public void 가게테이블_변경확인_테스트() throws Exception{
//        //given
//        //when
//        Store store = storeRepository.findByName("식당1").get();
//        //then
//        assertEquals(5, store.getTotalWaitingCount());   //전체 대기인원수가 느는지
//        assertEquals(25, store.getTotalWaitingTime());   //전체 대기시간이 느는지
//    }
//    @Test
//    @Order(3)
//    public void 같은회원_중복뽑기_불가능_테스트() throws Exception{
//        //given
//        //when
//        Ticket ticket = Ticket.builder()
//                .peopleCount(3)
//                .build();
//
//        Member member = memberRepository.findByUsername("admin").get();
//        Store store = storeRepository.findByMember_Id(member.getId()).get();
//
//        //then
//        Assertions.assertThrows(DuplicateTicketingException.class, () ->{
//            storeService.createTicket(ticket, store.getId(), "test1");
//        });
//    }
//
//    @Test
//    @Rollback(value = false)
//    @Order(2)
//    public void 번호표_발급_테스트2() throws Exception{
//        //given
//        //when
//        Ticket ticket = Ticket.builder()
//                .peopleCount(4)
//                .build();
//        Ticket ticket2 = Ticket.builder()
//                .peopleCount(3)
//                .build();
//        Ticket ticket3 = Ticket.builder()
//                .peopleCount(3)
//                .build();
//
//        Member member = memberRepository.findByUsername("admin").get();
//        Store store = storeRepository.findByMember_Id(member.getId()).get();
//
//        Ticket savedTicket = storeService.createTicket(ticket, store.getId(), "test3");
//        Ticket savedTicket2 = storeService.createTicket(ticket2, store.getId(), "test4");
//        Ticket savedTicket3 = storeService.createTicket(ticket3, store.getId(), "admin");
//
//        //then
//        assertEquals(3, savedTicket.getWaitingNum()); //번호표가 느는지 체크
//        assertEquals(15, savedTicket.getWaitingTime()); //번호표가 느는지 체크
//        assertEquals(4, savedTicket2.getWaitingNum()); //번호표가 느는지 체크
//        assertEquals(20, savedTicket2.getWaitingTime()); //번호표가 느는지 체크
//        assertEquals(5, savedTicket3.getWaitingNum()); //번호표가 느는지 체크
//        assertEquals(25, savedTicket3.getWaitingTime()); //번호표가 느는지 체크
//    }
//
//    @Test
//    @Rollback(value = false)
//    @Order(1)
//    public void 번호표_발급_테스트1() throws Exception{
//        //given
//        //when
//        Ticket ticket = Ticket.builder()
//                .peopleCount(1)
//                .build();
//
//        Member member = memberRepository.findByUsername("admin").get();
//        Store store = storeRepository.findByMember_Id(member.getId()).get();
//
//        Ticket savedTicket = storeService.createTicket(ticket, store.getId(), "test2");
//
//        //then
//        assertEquals(2, savedTicket.getWaitingNum()); //번호표가 느는지 체크
//        assertEquals(10, savedTicket.getWaitingTime()); //번호표가 느는지 체크
//    }
//
//    @Test
//    @Rollback(value = false)
//    @Order(0)
//    public void 번호표_발급_테스트0() throws Exception{
//        //given
//        //when
//        Ticket ticket = Ticket.builder()
//                .peopleCount(1)
//                .build();
//        Member member = memberRepository.findByUsername("admin").get();
//        Store store = storeRepository.findByMember_Id(member.getId()).get();
//
//        Ticket savedTicket = storeService.createTicket(ticket, store.getId(), "test1");
//
//        //then
//        assertEquals(1, savedTicket.getWaitingNum()); //번호표가 느는지 체크
//        assertEquals(5, savedTicket.getWaitingTime()); //번호표가 느는지 체크
//    }
//
//}