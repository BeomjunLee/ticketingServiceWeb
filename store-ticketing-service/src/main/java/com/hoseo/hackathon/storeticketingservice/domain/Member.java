package com.hoseo.hackathon.storeticketingservice.domain;
import com.hoseo.hackathon.storeticketingservice.domain.status.Role;
import jdk.jfr.BooleanFlag;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    
    @Column(unique = true)
    private String username;                                //아이디

    private String password;                                //비밀번호

    private String name;                                    //이름

    private String phoneNum;                                //전화번호

    private String email;                                   //이메일

    private int point;                                      //포인트

    private Boolean status;                                 //인증 상태

    @Enumerated(value = EnumType.STRING)
    private Role role;                                      //권한(일반회원, 가게 관리자, 사이트 관리자, 탈퇴회원)

    private LocalDateTime createdDate;                      //가입일
    private LocalDateTime deletedDate;                      //탈퇴일

    //회원 수정
    public void changeMember(String name, String phoneNum, String email) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
    }

    //회원 수정(관리자용)
    public void changeMemberByAdmin(String username, String name, String phoneNum, String email, int point) {
        this.username = username;
        this.name = name;
        this.phoneNum = phoneNum;
        this.email = email;
        this.point = point;
    }

    //비밀번호 암호화위해 setter
    public void encodingPassword(String password) {
        this.password = password;
    }

    //==비지니스 로직
    public void changeRole(Role role) { //권한 변경
        this.role = role;
    }

    //인증 상태
    public void changeEnabled(Boolean status) {
        this.status = status;
    }
}
