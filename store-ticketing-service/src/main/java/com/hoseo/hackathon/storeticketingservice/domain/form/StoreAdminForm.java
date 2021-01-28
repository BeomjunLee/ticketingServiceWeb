package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreAdminForm {
    @NotBlank(message = "아이디를 입력해주세요")
    private String memberUsername;                                //아이디

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String memberPassword;                                //비밀번호

    @NotBlank(message = "이름을 입력해주세요")
    private String memberName;                                    //이름

    @NotBlank(message = "전화번호를 입력해주세요")
    private String memberPhoneNum;                                //전화번호

    @Email(message = "이메일 형식을 지켜주세요")
    @NotBlank(message = "이메일을 입력해주세요")
    private String memberEmail;                                   //이메일

    @NotBlank(message = "가게 이름을 입력해주세요")
    private String storeName;                //이름 ->중복되면 안됨

    @NotBlank(message = "가게 전화번호를 입력해주세요")
    private String storePhoneNum;            //전화번호

    @NotNull(message = "주소를 입력해주세요")
    private String streetAddress;           //api에서 받은 도로명 주소

    @NotNull(message = "주소를 입력해주세요")
    private String detailAddress;           //api에서 받은 상세주소

    @NotBlank(message = "위도를 입력해주세요")
    private String storeLatitude;            //위도

    @NotBlank(message = "경도를 입력해주세요")
    private String storeLongitude;           //경도

    @NotBlank(message = "사업자 등록번호를 입력해주세요")
    private String storeCompanyNumber;       //사업자등록번호
}
