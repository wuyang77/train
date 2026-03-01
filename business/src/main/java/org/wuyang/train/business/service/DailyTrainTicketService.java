package org.wuyang.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wuyang.train.business.domain.DailyTrainTicket;
import org.wuyang.train.business.domain.DailyTrainTicketExample;
import org.wuyang.train.business.domain.TrainStation;
import org.wuyang.train.business.mapper.DailyTrainTicketMapper;
import org.wuyang.train.business.req.DailyTrainTicketQueryReq;
import org.wuyang.train.business.req.DailyTrainTicketSaveReq;
import org.wuyang.train.business.resp.DailyTrainTicketQueryResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class DailyTrainTicketService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainTicketService.class);

    @Resource
    private DailyTrainTicketMapper dailyTrainTicketMapper;

    @Resource
    private TrainStationService trainStationService;


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

    @Transactional
    public void generateDailyTrainTicketAll(Date date, String trainCode) {
        LOG.info("2.生成日期【{}】和车次【{}】的车次余票信息开始", DateUtil.formatDate(date), trainCode);

        LOG.info("删除日期【{}】和车次【{}】的所有每日车次余票信息", DateUtil.formatDate(date), trainCode);
        DailyTrainTicketExample dailyTrainTicketExample = new DailyTrainTicketExample();
        dailyTrainTicketExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainTicketMapper.deleteByExample(dailyTrainTicketExample);

        // 查出某车次的所有余票信息
        LOG.info("查出【{}】车次的所有车次余票信息", trainCode);
        List<TrainStation> trainStationList = trainStationService.selectTrainStationByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainStationList)) {
            LOG.info("【{}】车次没有车站基础数据，生成该车次余票信息结束", trainCode);
            return;
        }

        DateTime now = DateTime.now();
        for (int i = 0; i < trainStationList.size(); i++) {
            TrainStation trainStationStart = trainStationList.get(i);
            for (int j = (i + 1); j < trainStationList.size(); j++) {
                TrainStation trainStationEnd = trainStationList.get(j);
                DailyTrainTicket dailyTrainTicket = new DailyTrainTicket();
                dailyTrainTicket.setId(SnowUtil.getSnowflakeNextId());
                dailyTrainTicket.setDate(date);
                dailyTrainTicket.setTrainCode(trainCode);
                dailyTrainTicket.setStart(trainStationStart.getName());
                dailyTrainTicket.setStartPinyin(trainStationStart.getNamePinyin());
                dailyTrainTicket.setStartTime(trainStationStart.getOutTime());
                dailyTrainTicket.setStartIndex(trainStationStart.getIndex());
                dailyTrainTicket.setEnd(trainStationEnd.getName());
                dailyTrainTicket.setEndPinyin(trainStationEnd.getNamePinyin());
                dailyTrainTicket.setEndTime(trainStationEnd.getInTime());
                dailyTrainTicket.setEndIndex(trainStationEnd.getIndex());
                dailyTrainTicket.setYdz(0);
                dailyTrainTicket.setYdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setEdz(0);
                dailyTrainTicket.setEdzPrice(BigDecimal.ZERO);
                dailyTrainTicket.setRw(0);
                dailyTrainTicket.setRwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setYw(0);
                dailyTrainTicket.setYwPrice(BigDecimal.ZERO);
                dailyTrainTicket.setCreateTime(now);
                dailyTrainTicket.setUpdateTime(now);
                dailyTrainTicketMapper.insert(dailyTrainTicket);
            }
        }
    }
}
