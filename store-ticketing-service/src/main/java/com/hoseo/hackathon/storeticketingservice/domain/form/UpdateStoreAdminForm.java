package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UpdateStoreAdminForm {
    @NotBlank(message = "회원 이름을 입력해주세요")
    private String member_name;
    @NotBlank(message = "회원 전화번호를 입력해주세요")
    private String member_phoneNum;
    @NotBlank(message = "회원 이메일을 입력해주세요")
    @Email
    private String member_email;

    @NotBlank(message = "가게 전화번호를 입력해주세요")
    private String store_phoneNum;
    @NotBlank(message = "가게 주소를 입력해주세요")
    private String store_address;
}
