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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wuyang.train.business.domain.DailyTrainStation;
import org.wuyang.train.business.domain.DailyTrainStationExample;
import org.wuyang.train.business.domain.TrainStation;
import org.wuyang.train.business.mapper.DailyTrainStationMapper;
import org.wuyang.train.business.mapper.TrainStationMapper;
import org.wuyang.train.business.req.DailyTrainStationQueryReq;
import org.wuyang.train.business.req.DailyTrainStationSaveReq;
import org.wuyang.train.business.resp.DailyTrainStationQueryResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainStationService.class);

    @Resource
    private DailyTrainStationMapper dailyTrainStationMapper;
    @Autowired
    private TrainStationMapper trainStationMapper;
    @Autowired
    private TrainStationService trainStationService;

    public void saveOrEditDailyTrainStation(DailyTrainStationSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(req, DailyTrainStation.class);
        if (ObjectUtil.isNull(dailyTrainStation.getId())) {
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.insert(dailyTrainStation);
        } else {
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStationMapper.updateByPrimaryKey(dailyTrainStation);
        }
    }

    public PageResp<DailyTrainStationQueryResp> queryDailyTrainStationList(DailyTrainStationQueryReq req) {
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.setOrderByClause("date desc, train_code asc, `index` asc");
        DailyTrainStationExample.Criteria criteria = dailyTrainStationExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotNull(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<DailyTrainStation> dailyTrainStationList = dailyTrainStationMapper.selectByExample(dailyTrainStationExample);

        PageInfo<DailyTrainStation> pageInfo = new PageInfo<>(dailyTrainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<DailyTrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(dailyTrainStationList, DailyTrainStationQueryResp.class));
        return pageResp;
    }

    public void deleteDailyTrainStation(Long id) {
        dailyTrainStationMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void generateDailyTrainStationAll(Date date, String trainCode) {
        LOG.info("2.生成日期【{}】和车次【{}】的车次车站信息开始", DateUtil.formatDate(date), trainCode);

        LOG.info("删除日期【{}】和车次【{}】的所有每日车次信息", DateUtil.formatDate(date), trainCode);
        DailyTrainStationExample dailyTrainStationExample = new DailyTrainStationExample();
        dailyTrainStationExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainStationMapper.deleteByExample(dailyTrainStationExample);

        // 查出某车次的所有车站信息
        LOG.info("查出【{}】车次的所有车次车站信息", trainCode);
        List<TrainStation> trainStationList = trainStationService.selectTrainStationByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainStationList)) {
            LOG.info("【{}】车次没有车站基础数据，生成该车次车站信息结束", trainCode);
            return;
        }

        // 生成某日车次的所有每日车站信息
        LOG.info("生成成日期【{}】车次的所有每日车次车站信息", DateUtil.formatDate(date));
        for (TrainStation trainStation : trainStationList) {
            DateTime now = DateTime.now();
            DailyTrainStation dailyTrainStation = BeanUtil.copyProperties(trainStation, DailyTrainStation.class);
            dailyTrainStation.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainStation.setCreateTime(now);
            dailyTrainStation.setUpdateTime(now);
            dailyTrainStation.setDate(date);
            dailyTrainStationMapper.insert(dailyTrainStation);
        }
        LOG.info("2.生成日期【{}】和车次【{}】的车次车站信息结束", DateUtil.formatDate(date), trainCode);
    }

}
