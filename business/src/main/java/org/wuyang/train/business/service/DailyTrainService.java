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
import org.wuyang.train.business.domain.DailyTrain;
import org.wuyang.train.business.domain.DailyTrainExample;
import org.wuyang.train.business.domain.Train;
import org.wuyang.train.business.mapper.DailyTrainMapper;
import org.wuyang.train.business.req.DailyTrainQueryReq;
import org.wuyang.train.business.req.DailyTrainSaveReq;
import org.wuyang.train.business.resp.DailyTrainQueryResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainService.class);

    @Resource
    private DailyTrainMapper dailyTrainMapper;

    @Resource
    private TrainService trainService;
    @Autowired
    private DailyTrainStationService dailyTrainStationService;
    @Autowired
    private DailyTrainCarriageService dailyTrainCarriageService;
    @Autowired
    private DailyTrainSeatService dailyTrainSeatService;

    public void saveOrEditDailyTrain(DailyTrainSaveReq req) {
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(req, DailyTrain.class);
        if (ObjectUtil.isNull(dailyTrain.getId())) {
            dailyTrain.setId(SnowUtil.getSnowflakeNextId());
            dailyTrain.setCreateTime(now);
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.insert(dailyTrain);
        } else {
            dailyTrain.setUpdateTime(now);
            dailyTrainMapper.updateByPrimaryKey(dailyTrain);
        }
    }

    public PageResp<DailyTrainQueryResp> queryDailyTrainList(DailyTrainQueryReq req) {
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.setOrderByClause("date desc, code asc");
        DailyTrainExample.Criteria criteria = dailyTrainExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotEmpty(req.getCode())) {
            criteria.andCodeEqualTo(req.getCode());
        }

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<DailyTrain> dailyTrainList = dailyTrainMapper.selectByExample(dailyTrainExample);

        PageInfo<DailyTrain> pageInfo = new PageInfo<>(dailyTrainList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<DailyTrainQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(dailyTrainList, DailyTrainQueryResp.class));
        return pageResp;
    }

    public void deleteDailyTrain(Long id) {
        dailyTrainMapper.deleteByPrimaryKey(id);
    }

    /**
     * 生成某日所有车次信息，包括车次、车站、车厢、座位
     * @param date
     */
    public void generateDailyTrainAll(Date date) {
        LOG.info("查询到所有的车次信息");
        List<Train> trainList = trainService.selectTrainAll();
        if (CollUtil.isEmpty(trainList)) {
            LOG.info("没有车次基础数据，任务结束");
            return;
        }

        LOG.info("生成某日期下的所有车次的数据:1.每日车次信息。2.每日车次车站信息。3.每日车厢信息。4.每日座位信息。开始遍历每个车次");
        for (Train train : trainList) {
            generateSingleDailyTrain(date, train);
        }
    }

    @Transactional
    public void generateSingleDailyTrain(Date date, Train train) {
        LOG.info("生成日期【{}】车次【{}】的每日数据开始", DateUtil.formatDate(date), train.getCode());

        // 车次之间低耦合，车次内部高内聚
        LOG.info("删除【{}】车次已有的每日车次数据", train.getCode());
        DailyTrainExample dailyTrainExample = new DailyTrainExample();
        dailyTrainExample.createCriteria()
                .andDateEqualTo(date)
                .andCodeEqualTo(train.getCode());
        dailyTrainMapper.deleteByExample(dailyTrainExample);

        LOG.info("生成【{}】车次：每日车次数据", train.getCode());
        // 生成该车次的每日车次数据
        DateTime now = DateTime.now();
        DailyTrain dailyTrain = BeanUtil.copyProperties(train, DailyTrain.class);
        dailyTrain.setId(SnowUtil.getSnowflakeNextId());
        dailyTrain.setCreateTime(now);
        dailyTrain.setUpdateTime(now);
        dailyTrain.setDate(date);
        dailyTrainMapper.insert(dailyTrain);

        LOG.info("生成【{}】车次：每日车次车站信息", train.getCode());
        // 生成该车次的车站数据
        dailyTrainStationService.generateDailyTrainStationAll(date, train.getCode());

        LOG.info("生成【{}】车次：每日车厢信息", train.getCode());
        // 生成该车次的车厢数据
        dailyTrainCarriageService.generateDailyTrainCarriageAll(date, train.getCode());

        LOG.info("生成【{}】车次：每日座位信息", train.getCode());
        // 生成该车次的座位数据
        dailyTrainSeatService.generateDailyTrainSeatAll(date, train.getCode());

        LOG.info("1.生成日期【{}】车次【{}】的每日数据结束", DateUtil.formatDate(date), train.getCode());
    }
}
