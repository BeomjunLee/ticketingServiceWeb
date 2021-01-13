package com.hoseo.hackathon.storeticketingservice.domain;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
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
    private int nextEnterPhoneNum;      //다음 입장 회원 전화번호
    private int totalWaitingCount;       //전체 대기인원
    private int AvgWaitingTimeByOne;    //한 사람당 평균 대기시간
    private int totalWaitingTime;       //전체 대기시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;              //member_id

    @OneToMany(mappedBy = "store")
    private List<Ticket> ticketList = new ArrayList<>();    //Ticket 조회용

    //==연관관계 메서드==
    public void setMember(Member member) {
        this.member = member;
    }

    //==비지니스로직==
    //번호표 뽑을때 Store 변경점
    public void changeStoreByTicketing(int totalWaitingCount) {
        this.totalWaitingCount = totalWaitingCount + 1;     //전체 대기인원수 설정
        this.totalWaitingTime = this.AvgWaitingTimeByOne * (totalWaitingCount + 1); //전체 대기시간 설정
    }
    
    //번호표 취소, 넘기기 Store 변경점
    public void changeStoreByCancelOrNext(int totalWaitingCount) {
        this.totalWaitingCount = totalWaitingCount - 1;     //전체 대기인원수 설정
        this.totalWaitingTime = this.AvgWaitingTimeByOne * (totalWaitingCount - 1); //전체 대기시간 설정
    }
}
