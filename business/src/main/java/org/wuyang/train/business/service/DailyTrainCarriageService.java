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
import org.wuyang.train.business.domain.DailyTrainCarriage;
import org.wuyang.train.business.domain.DailyTrainCarriageExample;
import org.wuyang.train.business.domain.TrainCarriage;
import org.wuyang.train.business.enums.SeatColEnum;
import org.wuyang.train.business.mapper.DailyTrainCarriageMapper;
import org.wuyang.train.business.req.DailyTrainCarriageQueryReq;
import org.wuyang.train.business.req.DailyTrainCarriageSaveReq;
import org.wuyang.train.business.resp.DailyTrainCarriageQueryResp;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.util.Date;
import java.util.List;

@Service
public class DailyTrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(DailyTrainCarriageService.class);

    @Resource
    private DailyTrainCarriageMapper dailyTrainCarriageMapper;
    @Autowired
    private TrainCarriageService trainCarriageService;

    public void saveOrEditDailyTrainCarriage(DailyTrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(req, DailyTrainCarriage.class);
        if (ObjectUtil.isNull(dailyTrainCarriage.getId())) {
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        } else {
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriageMapper.updateByPrimaryKey(dailyTrainCarriage);
        }
    }

    public PageResp<DailyTrainCarriageQueryResp> queryDailyTrainCarriageList(DailyTrainCarriageQueryReq req) {
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.setOrderByClause("date desc, train_code asc, `index` asc");
        DailyTrainCarriageExample.Criteria criteria = dailyTrainCarriageExample.createCriteria();
        if (ObjectUtil.isNotNull(req.getDate())) {
            criteria.andDateEqualTo(req.getDate());
        }
        if (ObjectUtil.isNotNull(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<DailyTrainCarriage> dailyTrainCarriageList = dailyTrainCarriageMapper.selectByExample(dailyTrainCarriageExample);

        PageInfo<DailyTrainCarriage> pageInfo = new PageInfo<>(dailyTrainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<DailyTrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(dailyTrainCarriageList, DailyTrainCarriageQueryResp.class));
        return pageResp;
    }

    public void deleteDailyTrainCarriage(Long id) {
        dailyTrainCarriageMapper.deleteByPrimaryKey(id);
    }

    @Transactional
    public void generateDailyCarriageAll(Date date, String trainCode) {
        LOG.info("3.生成日期【{}】和车次【{}】的车厢信息开始", DateUtil.formatDate(date), trainCode);

        LOG.info("删除日期【{}】和车次【{}】的所有每日车厢信息", DateUtil.formatDate(date), trainCode);
        DailyTrainCarriageExample dailyTrainCarriageExample = new DailyTrainCarriageExample();
        dailyTrainCarriageExample.createCriteria()
                .andDateEqualTo(date)
                .andTrainCodeEqualTo(trainCode);
        dailyTrainCarriageMapper.deleteByExample(dailyTrainCarriageExample);

        // 查出某车次的所有车站信息
        LOG.info("查出【{}】车次的所有车厢信息", trainCode);
        List<TrainCarriage> trainCarriageList = trainCarriageService.selectCarrigesByTrainCode(trainCode);
        if (CollUtil.isEmpty(trainCarriageList)) {
            LOG.info("【{}】车次没有车站基础数据，生成该车厢信息结束", trainCode);
            return;
        }

        // 生成某日车次的所有每日车站信息
        LOG.info("生成成日期【{}】车次的所有每日车厢信息", DateUtil.formatDate(date));
        for (TrainCarriage trainCarriage : trainCarriageList) {
            DateTime now = new DateTime();
            DailyTrainCarriage dailyTrainCarriage = BeanUtil.copyProperties(trainCarriage, DailyTrainCarriage.class);
            dailyTrainCarriage.setId(SnowUtil.getSnowflakeNextId());
            dailyTrainCarriage.setCreateTime(now);
            dailyTrainCarriage.setUpdateTime(now);
            dailyTrainCarriage.setDate(date);
            dailyTrainCarriageMapper.insert(dailyTrainCarriage);
        }
        LOG.info("3.生成日期【{}】和车次【{}】的车厢信息结束", DateUtil.formatDate(date), trainCode);
    }
}
