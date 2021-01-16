package com.hoseo.hackathon.storeticketingservice.repository;

import com.hoseo.hackathon.storeticketingservice.domain.Ticket;
import com.hoseo.hackathon.storeticketingservice.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    //티켓 상태별 카운트
    int countByStore_IdAndStatus(Long store_id, TicketStatus Status);

    //관리자가 보류한 티켓들 select
    Page<Ticket> findAllByStore_IdAndStatus(Long store_id, TicketStatus status, Pageable pageable);

    //보류나 취소가 되지않는 한 가게의 티켓들 중에서 n번째 번호 찾기
    Optional<Ticket> findByStore_IdAndWaitingNumAndStatus(Long store_id, int waitingNum, TicketStatus status);

    //회원이 이미 티켓을 뽑은게있는지 확인
    int countByMemberUsername(String username);

    //취소한 사람의 뒤의 티켓을 - 1
    @Transactional
    @Modifying
    @Query("update Ticket t set t.waitingNum = t.waitingNum - 1, t.waitingTime = (t.waitingNum - 1) * :avgWaitingTime " +
            "where t.waitingNum > :waitingNum and t.status = :status and t.store.id = :store_id")
    void updateTicketsMinus1(@Param("status") TicketStatus status, @Param("waitingNum") int waitingNum, @Param("avgWaitingTime") int avgWaitingTime, @Param("store_id")Long store_id);
}
