package com.hoseo.hackathon.storeticketingservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminStoreManageDto {
    private PagedModel<EntityModel<StoreDto>> storeList;
    private int totalEnrollStoreCount;
}
