package com.hoseo.hackathon.storeticketingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    /**
     * 메인 페이지로
     */
    @GetMapping("/main")
    public String main() {
        return "main";
    }

    /**
     * 로그인 페이지
     */
    @GetMapping("/login")
    public String goLogin(){
        return "login";
    }
}
