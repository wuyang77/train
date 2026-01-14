package org.wuyang.member.service;

import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.wuyang.common.exception.BusinessException;
import org.wuyang.common.exception.BusinessExceptionEnum;
import org.wuyang.common.util.SnowUtil;
import org.wuyang.member.domain.Member;
import org.wuyang.member.domain.MemberExample;
import org.wuyang.member.mapper.MemberMapper;
import org.wuyang.member.resq.MemberRegisterReq;

import java.util.List;

@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    public Integer count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public Long register(MemberRegisterReq req) {

        String mobile = req.getMobile();
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        if (CollUtil.isNotEmpty(memberList)) {
//            return memberList.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());//参数1为终端ID 参数2为数据中心ID
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }
}
