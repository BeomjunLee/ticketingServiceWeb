package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.StoreController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.AdminMemberManageDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.AdminStoreManageDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminMemberManageResource extends EntityModel<AdminMemberManageDto> {
    public AdminMemberManageResource(AdminMemberManageDto dto, Link... links) {
        super(dto, links);
        add(linkTo(StoreController.class).slash("members").withSelfRel());
        add(linkTo(StoreController.class).slash("members/search").withRel("검색"));
        //TODO 검색 링크추가
    }
}
