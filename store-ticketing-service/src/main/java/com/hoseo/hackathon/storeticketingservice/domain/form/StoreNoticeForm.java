package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StoreNoticeForm {
    @NotBlank(message = "한글자 이상 입력해주세요")
    private String notice;

}
