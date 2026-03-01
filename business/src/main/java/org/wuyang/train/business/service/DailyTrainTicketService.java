package org.wuyang.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;
import org.wuyang.train.business.domain.DailyTrainTicket;
import org.wuyang.train.business.domain.DailyTrainTicketExample;
import org.wuyang.train.business.mapper.DailyTrainTicketMapper;
import org.wuyang.train.business.resp.DailyTrainTicketQueryResp;
import org.wuyang.train.business.req.DailyTrainTicketQueryReq;
import org.wuyang.train.business.req.DailyTrainTicketSaveReq;

import java.util.List;

@Service
public class DailyTrainTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    public void saveOrEditDailyTrainTicket(DailyTrainTicketSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainTicket dailyTrainTicket = BeanUtil.copyProperties(req, DailyTrainTicket.class);
        if (ObjectUtil.isNull(dailyTrainTicket.getId())) {
            dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainTicket.setCreateTime(now);
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.insert(dailyTrainTicket);
        } else {
            dailyTrainTicket.setUpdateTime(now);
            dailyTrainTicketMapper.updateByPrimaryKey(dailyTrainTicket);
        }
    }

    public PageResp<DailyTrainTicketQueryResp> queryDailyTrainTicketList(DailyTrainTicketQueryReq req) {
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.setOrderByClause("id desc");
        DailyTrainTicketExample.Criteria criteria = dailyTrainTicketExample.createCriteria();

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<DailyTrainTicket> dailyTrainTicketList = dailyTrainTicketMapper.selectByExample(dailyTrainTicketExample);

        PageInfo<DailyTrainTicket> pageInfo = new PageInfo<>(dailyTrainTicketList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<DailyTrainTicketQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(dailyTrainTicketList, DailyTrainTicketQueryResp.class));
        return pageResp;
    }

    public void deleteDailyTrainTicket(Long id) {
        dailyTrainTicketMapper.deleteByPrimaryKey(id);
    }
}
