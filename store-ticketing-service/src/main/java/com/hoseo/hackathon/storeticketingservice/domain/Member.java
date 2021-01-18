package com.hoseo.hackathon.storeticketingservice.domain;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private MemberStatus status;                            //가입 대기(VALID, INVALID)
    @Enumerated(value = EnumType.STRING)
    private Role role;                                      //권한


    //비밀번호 암호화위해 setter
    public void encodingPassword(String password) {
        this.password = password;
    }

    //==비지니스 로직
    public void changeRole(Role role) { //권한 변경
        this.role = role;
    }
    public void changeMemberStatus(MemberStatus status) {   //가입상태 변경
        this.status = status;
    }
}
