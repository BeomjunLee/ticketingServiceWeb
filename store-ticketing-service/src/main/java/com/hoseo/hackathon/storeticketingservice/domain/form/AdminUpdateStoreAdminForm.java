package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateStoreAdminForm {
    @NotBlank(message = "아이디를 입력해주세요")
    private String member_username;

    @NotBlank(message = "이름을 입력해주세요")
    private String member_name;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String member_phoneNum;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식을 지켜주세요")
    private String member_email;


    @NotBlank(message = "가게명을 입력해주세요")
    private String store_name;
    
    @NotBlank(message = "가게 전화번호를 입력해주세요")
    private String store_phoneNum;

    @NotBlank(message = "가게 주소를 입력해주세요")
    private String store_address;

    @NotBlank(message = "사업자 등록번호를 입력해주세요")
    private String store_companyNumber;
}
