package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberForm {
    @NotBlank(message = "아이디를 입력해주세요")
    private String username;                                //아이디
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;                                //비밀번호
    @NotBlank(message = "이름을 입력해주세요")
    private String name;                                    //이름
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNum;                                //전화번호
    @Email(message = "이메일 형식을 지켜주세요") 
    @NotBlank(message = "이메일을 입력해주세요")
    private String email;                                   //이메일
}
