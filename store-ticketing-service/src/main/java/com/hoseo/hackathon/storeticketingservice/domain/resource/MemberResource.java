package com.hoseo.hackathon.storeticketingservice.domain.resource;

import com.hoseo.hackathon.storeticketingservice.controller.MemberController;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class MemberResource extends EntityModel<MemberDto> {
    public MemberResource(MemberDto memberDTO, Link... links){
        super(memberDTO, links);
        add(linkTo(MemberController.class).slash(memberDTO.getId()).withSelfRel());
    }
}
