package com.hoseo.hackathon.storeticketingservice.domain.dto;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.status.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //기본 생성자 protected
public class MemberDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phoneNum;
    private String email;
    private int point;
    private Role role;

    public MemberDto(Member member){
        id = member.getId();
        username = member.getUsername();
        password = member.getPassword();
        name = member.getName();
        phoneNum = member.getPhoneNum();
        email = member.getEmail();
        point = member.getPoint();
        role = member.getRole();
    }
}