package com.hoseo.hackathon.storeticketingservice.repository.impl;

import com.hoseo.hackathon.storeticketingservice.domain.Member;
import com.hoseo.hackathon.storeticketingservice.domain.QMember;
import com.hoseo.hackathon.storeticketingservice.domain.QTicket;
import com.hoseo.hackathon.storeticketingservice.domain.condition.MemberSearchCondition;
import com.hoseo.hackathon.storeticketingservice.domain.dto.MemberListDto;
import com.hoseo.hackathon.storeticketingservice.domain.dto.QMemberListDto;
import com.hoseo.hackathon.storeticketingservice.repository.custom.MemberRepositoryCustom;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.hoseo.hackathon.storeticketingservice.domain.QMember.member;
import static com.hoseo.hackathon.storeticketingservice.domain.QTicket.ticket;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 회원 리스트 검색 동적
     * @param condition 아이디, 이름, 전화번호로 찾기
     * @return
     */
    @Override
    public Page<MemberListDto> search(MemberSearchCondition condition, Pageable pageable) {
        List<MemberListDto> content = queryFactory
                .select(new QMemberListDto(
                        ticket.id.as("ticket_id"),
                        member.id.as("member_id"),
                        member.username.as("username"),
                        member.name.as("name"),
                        member.phoneNum.as("phoneNum"),
                        member.email.as("email"),
                        member.createdDate.as("createdDate")))
                .from(member)
                .leftJoin(ticket).on(ticket.id.eq(member.id))
                .where(
                        member.status.eq(true),
                        usernameEq(condition.getUsername()),
                        nameEq(condition.getName()),
                        phoneNumEq(condition.getPhoneNum())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Member> countQuery = queryFactory
                .selectFrom(member)
                .where(member.status.eq(true));
        return PageableExecutionUtils.getPage(content, pageable,
                () -> countQuery.fetchCount());
    }

    private BooleanExpression usernameEq(String username) {
        if(hasText(username)) return member.username.eq(username);
        return null;
    }

    private BooleanExpression nameEq(String name) {
        if(hasText(name)) return member.name.eq(name);
        return null;
    }

    private BooleanExpression phoneNumEq(String phoneNum) {
        if(hasText(phoneNum)) return member.phoneNum.eq(phoneNum);
        return null;
    }

}
