package org.wuyang.member.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.wuyang.member.mapper.MemberMapper;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    public Integer count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }
}
