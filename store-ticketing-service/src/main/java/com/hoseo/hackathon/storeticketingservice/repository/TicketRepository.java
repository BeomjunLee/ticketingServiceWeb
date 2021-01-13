package com.hoseo.hackathon.storeticketingservice.repository;

import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //티켓 상태별 카운트
    int countByStatusEqualsAndStore_Id(TicketStatus ticketStatus, Long store_id);

    //회원이 이미 티켓을 뽑은게있는지 확인
    int countByMemberUsername(String username);

    //store_id 와 member_id로 select
    Optional<Ticket> findByStore_IdAndMember_Id(Long store_id, Long member_id);

    //취소한 사람의 뒤의 티켓을 - 1
    @Transactional
    @Modifying
    @Query("update Ticket t set t.waitingNum = t.waitingNum - 1, t.waitingTime = (t.waitingNum - 1) * :avgWaitingTime where t.waitingNum > :waitingNum and t.status = :status and t.store.id = :store_id")
    void updateTicketsMinus1(@Param("status") TicketStatus status, @Param("waitingNum") int waitingNum, @Param("avgWaitingTime") int avgWaitingTime, @Param("store_id")Long store_id);
}
