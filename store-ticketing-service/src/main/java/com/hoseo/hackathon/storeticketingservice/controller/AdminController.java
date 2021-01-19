package com.hoseo.hackathon.storeticketingservice.controller;

import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.domain.dto.*;
import com.hoseo.hackathon.storeticketingservice.domain.resource.*;
import com.hoseo.hackathon.storeticketingservice.domain.status.StoreStatus;
import com.hoseo.hackathon.storeticketingservice.service.AdminService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin", produces = MediaTypes.HAL_JSON_VALUE)
public class AdminController {
    private final AdminService adminService;

    /**
     * 가게 관리
     * 응답 : 가게이름, 전화번호, 주소, 등록일, 등록된 가게 수
     * link : 가게 번호표 관리, 가게수정, 가게관리자 정보보기, 이름으로 검색, 주소로 검색
     */
    @ApiOperation(value = "가게리스트 관리[사이트 관리자]", notes = "가게리스트를 조회하고 관리합니다")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stores")
    public ResponseEntity findStores(Pageable pageable, PagedResourcesAssembler<StoreDto> assembler) {
        int totalEnrollStoreCount = adminService.totalEnrollStoreCount();
        AdminStoreManageDto dto = AdminStoreManageDto.builder()
                .storeList(assembler.toModel(adminService.findStores(StoreStatus.VALID, pageable), e -> new AdminStoreListResource(e)))
                .totalEnrollStoreCount(totalEnrollStoreCount)
                .build();
        //hateoas
        AdminStoreManageResource resource = new AdminStoreManageResource(dto);
        return ResponseEntity.ok(resource);
    }

    /**
     * 대기중인 회원 리스트 관리
     * 응답 : 대기중인 회원정보,  가게 현재 상태, 총 대기인원, 총 대기시간, 공지사항, 한사람당 대기시간,
     * link : self, 대기 보류 취소 체크,  가게 번호표 활성화, 가게 번호표 비활성화, 공지사항 수정, 한사람당 대기시간 수정
     */
    @ApiOperation(value = "대기 인원 관리 + 가게 현황 관리[사이트 관리자]", notes = "사이트 관리자가 원하는 가게의 대기 인원을 관리합니다")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stores/{store_id}")
    public ResponseEntity manageAdminStore(@PathVariable("store_id") Long store_id, Pageable pageable, PagedResourcesAssembler<WaitingMembersDto> assembler) {
        Store store = adminService.findStore(store_id);
        StoreManageDto dto = StoreManageDto.builder()
                .waitingMembers(assembler.toModel(adminService.findWaitingMembers(store_id, pageable), e -> new WaitingMembersResource(e)))
                .storeTicketStatus(store.getStoreTicketStatus())
                .totalWaitingCount(store.getTotalWaitingCount())
                .totalWaitingTime(store.getTotalWaitingTime())
                .notice(store.getNotice())
                .avgWaitingTimeByOne(store.getAvgWaitingTimeByOne())
                //TODO 보류회원 결정나면 작업
                .build();
        //hateoas
        WaitingMembersAndStoreManageResource resource = new WaitingMembersAndStoreManageResource(dto);
        return ResponseEntity.ok(resource);
    }
    /**
     * 보류된 회원 리스트 관리
     * 응답 ; 이름, 전화번호
     * link : 보류 ticket별 취소, 체크,
     */
    @ApiOperation(value = "보류 인원 관리[사이트 관리자]", notes = "사이트 관리자가 원하는 가게의 보류 인원 명단을 관리합니다")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stores/{store_id}/holding")
    public ResponseEntity manageHoldMembers(@PathVariable("store_id") Long store_id, Pageable pageable, PagedResourcesAssembler<HoldingMembersDto> assembler) {
        //보류회원정보
        PagedModel<EntityModel<HoldingMembersDto>> holdingMembers =
                assembler.toModel(adminService.findHoldMembers(store_id, pageable), e -> new HoldingMembersResource(e));
        
        return ResponseEntity.ok(holdingMembers);
    }

    /**
     * 가게 수정
     */

    /**
     * 가게관리자 정보보기
     */


}
