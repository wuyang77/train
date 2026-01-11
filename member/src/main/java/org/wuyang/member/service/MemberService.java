package org.wuyang.member.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.wuyang.member.domain.Member;
import org.wuyang.member.domain.MemberExample;
import org.wuyang.member.mapper.MemberMapper;

import java.util.List;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    public Integer count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public Long register(String mobile) {

        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        if (CollUtil.isNotEmpty(memberList)) {
//            return memberList.get(0).getId();
            throw new RuntimeException("手机号已注册");
        }

        Member member = new Member();
        member.setId(System.currentTimeMillis());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
