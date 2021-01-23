package com.hoseo.hackathon.storeticketingservice.domain.resource.admin;

import com.hoseo.hackathon.storeticketingservice.controller.AdminController;
import com.hoseo.hackathon.storeticketingservice.controller.StoreController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreManageDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminWaitingMembersAndStoreManageResource extends EntityModel<StoreManageDto> { //가게 관리 메서드에 사용
    public AdminWaitingMembersAndStoreManageResource(StoreManageDto dto, Link... links){
        super(dto, links);
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("status").withRel("   번호표 OPEN"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("status").withRel("번호표 CLOSE"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("errors").withRel("오류 접수"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("--").withRel("공지사항 수정"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("--").withRel("한사람당 대기시간 수정"));
    }
}
