package com.hoseo.hackathon.storeticketingservice.domain;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id @GeneratedValue
    @Column(name = "store_id")
    private Long id;
    @Column(unique = true)
    private String name;                //이름 ->중복되면 안됨
    private String phoneNum;            //전화번호
    private String latitude;            //위도
    private String longitude;           //경도
    private int totalWaitingCount;       //전체 대기인원
    private int avgWaitingTimeByOne;    //한 사람당 평균 대기시간
    private int totalWaitingTime;       //전체 대기시간
    private String notice;              //공지사항
    private String companyNumber;       //사업자등록번호
    @Enumerated(EnumType.STRING)
    private StoreTicketStatus storeTicketStatus;   //가게 번호표 발급 활성화 (OPEN, CLOSE) 상태
    @Enumerated(EnumType.STRING)
    private StoreStatus storeStatus;    //가게 승인 여부 (VALID, INVALID)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              //member_id

    //==연관관계 메서드==
    public void setMember(Member member) {
        this.member = member;
    }

    //==비지니스로직==
    //번호표 뽑을때 Store 변경점
    public void changeStoreByTicketing(int totalWaitingCount) {
        this.totalWaitingCount = totalWaitingCount + 1;     //전체 대기인원수 설정
        this.totalWaitingTime = this.avgWaitingTimeByOne * (totalWaitingCount + 1); //전체 대기시간 설정
    }
    
    //번호표 취소, 넘기기 Store 변경점
    public void changeStoreByCancelOrNext(int totalWaitingCount) {
        this.totalWaitingCount = totalWaitingCount - 1;     //전체 대기인원수 설정
        this.totalWaitingTime = this.avgWaitingTimeByOne * (totalWaitingCount - 1); //전체 대기시간 설정
    }

    //번호표 발급 활성화 비활성화 변경
    public void changeStoreTicketStatus(StoreTicketStatus storeTicketStatus) {
        this.storeTicketStatus = storeTicketStatus;
    }

    //가게 승인 여부 변경
    public void changeStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }
}
