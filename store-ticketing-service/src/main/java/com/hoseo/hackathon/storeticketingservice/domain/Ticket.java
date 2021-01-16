package com.hoseo.hackathon.storeticketingservice.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {

    @Id
    @GeneratedValue
    @Column(name = "ticket_id")
    private Long id;

    private int peopleCount;             //인원수
    private int waitingNum;             //대기번호
    private int waitingTime;            //대기시간
    private LocalDateTime createdDate;   //발급시간

    @Enumerated(EnumType.STRING)
    private TicketStatus status;        //티켓 유효 상태(valid, invalid, cancel)
    private String notice;                //가게 알림창

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;                //store_id

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //항목 추가 메서드
    public void changeTicket(int peopleCount, int waitingNum, int waitingTime, LocalDateTime createdDate, TicketStatus status, String notice) {
        this.peopleCount = peopleCount;
        this.waitingNum = waitingNum;
        this.waitingTime = waitingTime;
        this.createdDate = createdDate;
        this.status = status;
        this.notice = notice;
    }
    
    //==연관 관계 편의 메서드

    public void setStore(Store store) {
        if (this.store != null) {
            store.getTicketList().remove(this);
        }
        this.store = store;
        store.getTicketList().add(this);
    }
    //==연관관계 편의메서드
    public void setMember(Member member) {
        this.member = member;
        member.setTicket(this);
    }

    //==비지니스 로직==
    //티켓 취소
    public void changeStatusTicket(TicketStatus status) {
        this.status = status;
    }

}
