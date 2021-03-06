package com.hoseo.hackathon.storeticketingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberListDto {
    private Long ticket_id;
    private Long member_id;
    
    private String username;                   //아이디
    private String name;                        //이름
    private String phoneNum;                    //전화번호
    private String email;                           //이메일
    private LocalDateTime createdDate;             //가입일자
}
