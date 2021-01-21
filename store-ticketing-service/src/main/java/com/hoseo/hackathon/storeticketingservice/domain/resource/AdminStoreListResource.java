package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.AdminController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreListDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminStoreListResource extends EntityModel<StoreListDto> {
    public AdminStoreListResource(StoreListDto dto, Link... links) {
        super(dto, links);
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).withRel("가게 번호표 관리"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).withRel("가게 수정"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash(dto.getMember_id()).withRel("가게 관리자 정보보기"));
    }
}
