package com.hoseo.hackathon.storeticketingservice.domain.dto.admin;

import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreListDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStoreManageDto {
    private Page<StoreListDto> storeList;
    private int totalStoreCount;
}
