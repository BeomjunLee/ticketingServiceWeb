package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreForm {
    @NotBlank(message = "가게 이름을 입력해주세요")
    private String name;                //이름 ->중복되면 안됨
    @NotBlank(message = "가게 전화번호를 입력해주세요")
    private String phoneNum;            //전화번호
    @NotBlank(message = "위도를 입력해주세요")
    private String latitude;            //위도
    @NotBlank(message = "경도를 입력해주세요")
    private String longitude;           //경도
    @NotBlank(message = "사업자 등록번호를 입력해주세요")
    private String companyNumber;       //사업자등록번호
}
