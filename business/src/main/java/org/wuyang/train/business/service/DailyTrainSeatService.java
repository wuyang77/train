package org.wuyang.train.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wuyang.train.business.domain.DailyTrainSeat;
import org.wuyang.train.business.domain.DailyTrainSeatExample;
import org.wuyang.train.business.domain.TrainSeat;
import org.wuyang.train.business.domain.TrainStation;
import org.wuyang.train.business.mapper.DailyTrainSeatMapper;
import org.wuyang.train.business.req.DailyTrainSeatQueryReq;
import org.wuyang.train.business.req.DailyTrainSeatSaveReq;
import org.wuyang.train.business.resp.DailyTrainSeatQueryResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainSeatService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainSeatService.class);

    @Resource
    private DailyTrainSeatMapper dailyTrainSeatMapper;

    @Resource
    private TrainSeatService trainSeatService;
    @Autowired
    private TrainStationService trainStationService;

    public void saveOrEditDailyTrainSeat(DailyTrainSeatSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(req, DailyTrainSeat.class);
        if (ObjectUtil.isNull(dailyTrainSeat.getId())) {
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        } else {
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeatMapper.updateByPrimaryKey(dailyTrainSeat);
        }
    }

    public PageResp<DailyTrainSeatQueryResp> queryDailyTrainSeatList(DailyTrainSeatQueryReq req) {
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.setOrderByClause("date desc, train_code asc, carriage_index asc, carriage_seat_index asc");
        DailyTrainSeatExample.Criteria criteria = dailyTrainSeatExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<DailyTrainSeat> dailyTrainSeatList = dailyTrainSeatMapper.selectByExample(dailyTrainSeatExample);

        PageInfo<DailyTrainSeat> pageInfo = new PageInfo<>(dailyTrainSeatList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<DailyTrainSeatQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(dailyTrainSeatList, DailyTrainSeatQueryResp.class));
        return pageResp;
    }

    public void deleteDailyTrainSeat(Long id) {
        dailyTrainSeatMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void generateDailyTrainSeatAll(Date date, String trainCode) {
        LOG.info("3.生成日期【{}】和车次【{}】的座位信息开始", DateUtil.formatDate(date), trainCode);

        LOG.info("删除日期【{}】和车次【{}】的所有每日座位信息", DateUtil.formatDate(date), trainCode);
        DailyTrainSeatExample dailyTrainSeatExample = new DailyTrainSeatExample();
        dailyTrainSeatExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainSeatMapper.deleteByExample(dailyTrainSeatExample);

        // 售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖
        List<TrainStation> trainStationList = trainStationService.selectTrainStationByTrainCode(trainCode);
        String sell = StrUtil.fillBefore("", '0', trainStationList.size() - 1);

        // 查出某车次的所有车站信息
        LOG.info("查出【{}】车次的所有座位信息", trainCode);
        List<TrainSeat> trainSeatList = trainSeatService.selectTrainSeatsByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainSeatList)) {
            LOG.info("【{}】车次没有车站基础数据，生成该座位信息结束", trainCode);
            return;
        }

        // 生成某日车次的所有每日车站信息
        LOG.info("生成成日期【{}】车次的所有每日座位信息", DateUtil.formatDate(date));
        for (TrainSeat trainSeat : trainSeatList) {
            DateTime now = DateTime.now();
            DailyTrainSeat dailyTrainSeat = BeanUtil.copyProperties(trainSeat, DailyTrainSeat.class);
            dailyTrainSeat.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainSeat.setCreateTime(now);
            dailyTrainSeat.setUpdateTime(now);
            dailyTrainSeat.setDate(date);
            dailyTrainSeat.setSell(sell);
            dailyTrainSeatMapper.insert(dailyTrainSeat);
        }

        LOG.info("3.生成日期【{}】和车次【{}】的座位信息结束", DateUtil.formatDate(date), trainCode);
    }
}
