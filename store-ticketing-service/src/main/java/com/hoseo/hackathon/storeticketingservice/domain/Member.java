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

    @Enumerated(value = EnumType.STRING)
    private Role role;                                      //권한
    
    @OneToOne(fetch = FetchType.LAZY, mappedBy =  "member")
    private Ticket ticket;                                  //ticket_id

    protected void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    //비밀번호 암호화위해 setter
    public void encodingPassword(String password) {
        this.password = password;
    }
}
