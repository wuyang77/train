package org.wuyang.member.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.wuyang.common.context.LoginMemberContext;
import org.wuyang.common.util.SnowUtil;
import org.wuyang.member.domain.Passenger;
import org.wuyang.member.mapper.PassengerMapper;
import org.wuyang.member.resq.PassengerSaveReq;

@Service
public class PassengerService {

    @Resource
    private PassengerMapper passengerMapper;

    public void savePassenger(PassengerSaveReq req) {
        DateTime now = DateTime.now();
        Passenger passenger = BeanUtil.copyProperties(req, Passenger.class);
        if (ObjectUtil.isNull(passenger.getId())) {
            passenger.setMemberId(LoginMemberContext.getId());
            passenger.setId(SnowUtil.getSnowflakeNextId());
            passenger.setCreateTime(now);
            passenger.setUpdateTime(now);
            passengerMapper.insert(passenger);
        } else {
            passenger.setUpdateTime(now);
            passengerMapper.updateByPrimaryKey(passenger);
        }
    }
}
