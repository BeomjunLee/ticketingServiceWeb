package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.MemberController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreAdminDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class StoreAdminResource extends EntityModel<StoreAdminDto> {
    public StoreAdminResource(StoreAdminDto dto, Link... links){
        super(dto, links);
        add(linkTo(MemberController.class).slash(dto.getMember_id()).withSelfRel());
        add(linkTo(MemberController.class).slash(dto.getMember_id()).withRel("회원 수정"));
        add(linkTo(MemberController.class).slash(dto.getMember_id()).withRel("회원 탈퇴"));
    }
}
