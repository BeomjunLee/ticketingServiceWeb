package com.hoseo.hackathon.storeticketingservice.domain.form;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class UpdatePasswordForm {
    @NotBlank(message = "현재 비밀번호를 입력해주세요")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력해주세요")
    private String newPassword;
}
