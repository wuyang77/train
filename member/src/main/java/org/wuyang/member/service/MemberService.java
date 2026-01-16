package org.wuyang.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wuyang.common.exception.BusinessException;
import org.wuyang.common.exception.BusinessExceptionEnum;
import org.wuyang.common.resp.MemberLoginResp;
import org.wuyang.common.util.JwtUtil;
import org.wuyang.common.util.SnowUtil;
import org.wuyang.member.domain.Member;
import org.wuyang.member.domain.MemberExample;
import org.wuyang.member.mapper.MemberMapper;
import org.wuyang.member.resq.MemberLoginReq;
import org.wuyang.member.resq.MemberRegisterReq;
import org.wuyang.member.resq.MemberSendCodeReq;

import java.util.List;
import java.util.Objects;

@Service
public class MemberService {
    private static final Logger LOG = LoggerFactory.getLogger(MemberService.class);

    @Resource
    private MemberMapper memberMapper;

    public Integer count() {
        return Math.toIntExact(memberMapper.countByExample(null));
    }

    public Long register(MemberRegisterReq req) {

        String mobile = req.getMobile();
        Member memberDB = findMemberByMobile(mobile);
        if (Objects.isNull(memberDB)) {
//            return memberList.get(0).getId();
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_EXIST);
        }

        Member member = new Member();
        member.setId(SnowUtil.getSnowflakeNextId());
        member.setMobile(mobile);
        memberMapper.insert(member);
        return member.getId();
    }

    public void sendCode(MemberSendCodeReq req) {
        String mobile = req.getMobile();
        Member memberDB = findMemberByMobile(mobile);
        if (Objects.isNull(memberDB)) {
            // 手机号不存在，则插入手机号
            Member member = new Member();
            member.setId(SnowUtil.getSnowflakeNextId());
            member.setMobile(mobile);
            memberMapper.insert(member);
        } else {
            LOG.info("手机号存在，不插入记录");
        }

        //生成验证码
//        String code = RandomUtil.randomString(4);
        String code ="8888";
        LOG.info("生成短信验证码：{}", code);
        // 保存短信记录表：手机号，短信验证码，有效期，是否已使用，业务类型，发送时间，使用时间
        LOG.info("保存短信记录表");
        // 对接短信通道，发送短信
        LOG.info("对接短信通道");
    }

    public MemberLoginResp login(MemberLoginReq req) {
        String mobile = req.getMobile();
        String code = req.getCode();
        // 如果手机号不存在，则表示手机号未注册
        if (Objects.isNull(mobile)) {
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_NOT_EXIST);
        }
        //验证码对比校验
        // 假设从阿里云短信业务中获取的验证码固定为8888，这里不做随机生成
        if(!"8888".equals(code)){
            throw new BusinessException(BusinessExceptionEnum.MEMBER_MOBILE_CODE_ERROR);
        }
        MemberLoginResp memberLoginResp = BeanUtil.copyProperties(req, MemberLoginResp.class);
        String token = JwtUtil.createToken(memberLoginResp.getId(), memberLoginResp.getMobile());
        memberLoginResp.setToken(token);
        return memberLoginResp;
    }

    private Member findMemberByMobile(String mobile) {
        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        if (CollUtil.isEmpty(memberList)) {
            return null;
        } else {
            return memberList.get(0);
        }
    }
}
