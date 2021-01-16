package com.hoseo.hackathon.storeticketingservice.repository;

import com.hoseo.hackathon.storeticketingservice.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    //가게 이름으로 Store찾기
    Optional<Store> findByName(String name);

    //member_id로 select
    Optional<Store> findByMember_Id(Long member_id);
}
