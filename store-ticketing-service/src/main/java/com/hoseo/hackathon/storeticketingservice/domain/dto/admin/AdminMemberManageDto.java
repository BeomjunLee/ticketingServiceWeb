package com.hoseo.hackathon.storeticketingservice.domain.dto.admin;

import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberListDto;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class AdminMemberManageDto {
    private int totalMemberCount;           //전체 회원수
    private int currentUsingServiceCount;   //현재 서비스 이용자수
    private Page<MemberListDto> memberList;  //회원리스트
}
