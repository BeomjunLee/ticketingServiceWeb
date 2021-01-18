package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.StoreController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MembersAndTicketsDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class HoldingMembersResource extends EntityModel<MembersAndTicketsDto> {
    public HoldingMembersResource(MembersAndTicketsDto dto, Link... links){
        super(dto, links);
        add(linkTo(StoreController.class).slash("tickets").slash(dto.getTicket_id()).slash("holding").withRel("보류"));
        add(linkTo(StoreController.class).slash("tickets").slash(dto.getTicket_id()).slash("holding").withRel("취소"));
        add(linkTo(StoreController.class).slash("tickets").slash(dto.getTicket_id()).slash("holding").withRel("체크"));
    }

}
