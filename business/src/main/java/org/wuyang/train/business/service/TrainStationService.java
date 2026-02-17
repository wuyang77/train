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
import org.wuyang.train.business.domain.TrainStation;
import org.wuyang.train.business.domain.TrainStationExample;
import org.wuyang.train.business.mapper.TrainStationMapper;
import org.wuyang.train.business.req.TrainStationQueryReq;
import org.wuyang.train.business.req.TrainStationSaveReq;
import org.wuyang.train.business.resp.TrainStationQueryResp;
import org.wuyang.train.common.exception.BusinessException;
import org.wuyang.train.common.exception.BusinessExceptionEnum;
import org.wuyang.train.common.resp.PageResp;
import org.wuyang.train.common.util.SnowUtil;

import java.util.List;

@Service
public class TrainStationService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainStationService.class);

    @Resource
    private TrainStationMapper trainStationMapper;

    public void saveOrEditTrainStation(TrainStationSaveReq req) {
        DateTime now = DateTime.now();

        // 保存之前，先校验唯一键是否存在
        TrainStation trainStationDB = selectByUnique(req.getTrainCode(), req.getIndex());
        if (ObjectUtil.isNotEmpty(trainStationDB)) {
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_INDEX_UNIQUE_ERROR);
        }
        // 保存之前，先校验唯一键是否存在
        trainStationDB = selectByUnique(req.getTrainCode(), req.getName());
        if (ObjectUtil.isNotEmpty(trainStationDB)) {
            throw new BusinessException(BusinessExceptionEnum.BUSINESS_TRAIN_STATION_NAME_UNIQUE_ERROR);
        }

        TrainStation trainStation = BeanUtil.copyProperties(req, TrainStation.class);
        if (ObjectUtil.isNull(trainStation.getId())) {
            trainStation.setId(SnowUtil.getSnowflakeNextId());
            trainStation.setCreateTime(now);
            trainStation.setUpdateTime(now);
            trainStationMapper.insert(trainStation);
        } else {
            trainStation.setUpdateTime(now);
            trainStationMapper.updateByPrimaryKey(trainStation);
        }
    }

    private TrainStation selectByUnique(String uniqueTrainCode, Integer uniqueIndex) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(uniqueTrainCode)
                .andIndexEqualTo(uniqueIndex);
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);
        if (ObjectUtil.isNotEmpty(trainStationList)) {
            return trainStationList.get(0);
        } else {
            return null;
        }
    }

    private TrainStation selectByUnique(String uniqueTrainCode, String uniqueName) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.createCriteria()
                .andTrainCodeEqualTo(uniqueTrainCode)
                .andNameEqualTo(uniqueName);
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);
        if (ObjectUtil.isNotEmpty(trainStationList)) {
            return trainStationList.get(0);
        } else {
            return null;
        }
    }



    public PageResp<TrainStationQueryResp> queryTrainStationList(TrainStationQueryReq req) {
        TrainStationExample trainStationExample = new TrainStationExample();
        trainStationExample.setOrderByClause("train_code asc, `index` asc");
        TrainStationExample.Criteria criteria = trainStationExample.createCriteria();
        if (ObjectUtil.isNotEmpty(req.getTrainCode())) {
            criteria.andTrainCodeEqualTo(req.getTrainCode());
        }

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<TrainStation> trainStationList = trainStationMapper.selectByExample(trainStationExample);

        PageInfo<TrainStation> pageInfo = new PageInfo<>(trainStationList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<TrainStationQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(trainStationList, TrainStationQueryResp.class));
        return pageResp;
    }

    public void deleteTrainStation(Long id) {
        trainStationMapper.deleteByPrimaryKey(id);
    }
}
