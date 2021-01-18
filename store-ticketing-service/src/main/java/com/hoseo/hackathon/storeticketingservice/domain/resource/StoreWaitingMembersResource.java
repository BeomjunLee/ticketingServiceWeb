package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.StoreController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.StoreWaitingMembersDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class StoreWaitingMembersResource extends EntityModel<StoreWaitingMembersDto> {
    public StoreWaitingMembersResource(StoreWaitingMembersDto dto, Link... links){
        super(dto, links);
        add(linkTo(StoreController.class).slash("tickets").withSelfRel());
        add(linkTo(StoreController.class).slash("tickets").withRel("번호표 OPEN"));
        add(linkTo(StoreController.class).slash("tickets").withRel("번호표 CLOSE"));
        add(linkTo(StoreController.class).slash("tickets").withRel("공지사항 수정"));
        add(linkTo(StoreController.class).slash("tickets").withRel("한사람당 대기시간 수정"));
    }
}
