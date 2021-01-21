package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.AdminController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberListDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreListDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminMemberListResource extends EntityModel<MemberListDto> {
    public AdminMemberListResource(MemberListDto dto, Link... links) {
        super(dto, links);
        add(linkTo(AdminController.class).slash("members").slash(dto.getMember_id()).withRel("회원 탈퇴"));
        add(linkTo(AdminController.class).slash("members").slash(dto.getMember_id()).withRel("회원 수정"));
        add(linkTo(AdminController.class).slash("members").slash(dto.getTicket_id()).slash(dto.getMember_id()).withRel("가게 관리자 정보보기"));
    }
}
