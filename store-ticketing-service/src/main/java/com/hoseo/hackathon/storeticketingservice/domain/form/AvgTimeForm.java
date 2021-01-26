package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AvgTimeForm {

    @NotNull(message = "1이상 정수만 입력가능합니다")
    @Min(1)
    private int avgWaitingTimeByOne;
}
