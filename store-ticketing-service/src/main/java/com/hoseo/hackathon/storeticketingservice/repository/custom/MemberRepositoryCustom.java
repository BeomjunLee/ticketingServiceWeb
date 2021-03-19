package com.hoseo.hackathon.storeticketingservice.repository.custom;

import com.hoseo.hackathon.storeticketingservice.domain.condition.MemberSearchCondition;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<MemberListDto> search(MemberSearchCondition condition, Pageable pageable);
}
