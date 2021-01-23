package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class StoreNoticeAvgTimeForm {
    @NotBlank(message = "한글자 이상 입력해주세요")
    private String notice;

    @NotNull(message = "1이상 정수만 입력가능합니다")
    @Min(1)
    private int avgWaitingTimeByOne;
}
