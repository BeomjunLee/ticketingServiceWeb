package com.hoseo.hackathon.storeticketingservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreTicketStatus;
import lombok.*;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
@Builder
public class StoreManageDto {
    private Page<WaitingMembersDto> waitingMembers;    //대기회원정보
    private StoreTicketStatus storeTicketStatus;        //가게 현재상태
    private int totalWaitingCount;            //전체 대기인원
    private int totalWaitingTime;           //전체 대기시간
    private String notice;                  //공지사항
    private int avgWaitingTimeByOne;        //한사람당 평균 대기시간
//    private int holdMemberCount;            //보류회원수

    private Long store_id;
}
