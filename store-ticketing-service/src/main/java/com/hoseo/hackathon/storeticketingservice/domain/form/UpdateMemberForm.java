package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class UpdateMemberForm {
    @NotBlank(message = "이름을 입력해주세요")
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요")
    private String phoneNum;
    @NotBlank(message = "이메일을 입력해주세요")
    @Email
    private String email;
}
