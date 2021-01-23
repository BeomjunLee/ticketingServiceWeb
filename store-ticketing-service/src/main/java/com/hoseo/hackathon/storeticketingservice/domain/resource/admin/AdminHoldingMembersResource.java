package com.hoseo.hackathon.storeticketingservice.domain.resource.admin;

import com.hoseo.hackathon.storeticketingservice.controller.AdminController;
import com.hoseo.hackathon.storeticketingservice.controller.StoreController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.HoldingMembersDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class AdminHoldingMembersResource extends EntityModel<HoldingMembersDto> {
    public AdminHoldingMembersResource(HoldingMembersDto dto, Link... links){
        super(dto, links);
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("tickets").slash(dto.getTicket_id()).slash("hold").withRel("취소"));
        add(linkTo(AdminController.class).slash("stores").slash(dto.getStore_id()).slash("tickets").slash(dto.getTicket_id()).slash("hold").withRel("체크"));
    }

}
