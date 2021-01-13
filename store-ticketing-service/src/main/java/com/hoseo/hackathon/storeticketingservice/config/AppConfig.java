package com.hoseo.hackathon.storeticketingservice.config;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.Role;
import com.hoseo.hackathon.storeticketingservice.domain.Store;
import com.hoseo.hackathon.storeticketingservice.repository.StoreRepository;
import com.hoseo.hackathon.storeticketingservice.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        //여러가지 암호화 방법들을 알아서 매칭
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    //어플리케이션 시작시 test 계정 생성
    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            MemberService memberService;
            @Autowired
            StoreRepository storeRepository;
            @Override
            public void run(ApplicationArguments args) throws Exception {
                Member member = Member.builder()
                        .username("admin")
                        .password("1234")
                        .role(Role.ADMIN)
                        .build();

                Member member2 = Member.builder()
                        .username("test")
                        .password("1234")
                        .role(Role.USER)
                        .build();

                Member member3 = Member.builder()
                        .username("test2")
                        .password("1234")
                        .role(Role.USER)
                        .build();

                Member member4 = Member.builder()
                        .username("test3")
                        .password("1234")
                        .role(Role.USER)
                        .build();

                Store store = Store.builder()
                        .name("식당1")
                        .AvgWaitingTimeByOne(5)
                        .build();
                store.setMember(member);

                memberService.save(member);
                memberService.save(member2);
                memberService.save(member3);
                memberService.save(member4);
                storeRepository.save(store);
            }
        };
    }
}
