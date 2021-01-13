package com.hoseo.hackathon.storeticketingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.MediaTypes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/api", produces = MediaTypes.HAL_JSON_VALUE)
public class HomeController {
}
