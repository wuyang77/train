package org.wuyang.business.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wuyang.business.domain.TrainCarriage;
import org.wuyang.business.domain.TrainCarriageExample;
import org.wuyang.business.enums.SeatColEnum;
import org.wuyang.business.mapper.TrainCarriageMapper;
import org.wuyang.business.req.TrainCarriageQueryReq;
import org.wuyang.business.req.TrainCarriageSaveReq;
import org.wuyang.business.resp.TrainCarriageQueryResp;
import org.wuyang.common.resp.PageResp;
import org.wuyang.common.util.SnowUtil;

import java.util.List;

@Service
public class TrainCarriageService {

    private static final Logger LOG = LoggerFactory.getLogger(TrainCarriageService.class);

    @Resource
    private TrainCarriageMapper trainCarriageMapper;

    public void saveOrEditTrainCarriage(TrainCarriageSaveReq req) {
        DateTime now = DateTime.now();

        // 自动计算出列数和总座位数
        List<SeatColEnum> seatColEnums = SeatColEnum.getColsByType(req.getSeatType());
        req.setColCount(seatColEnums.size());
        req.setSeatCount(req.getColCount() * req.getRowCount());

        TrainCarriage trainCarriage = BeanUtil.copyProperties(req, TrainCarriage.class);
        if (ObjectUtil.isNull(trainCarriage.getId())) {
            trainCarriage.setId(SnowUtil.getSnowflakeNextId());
            trainCarriage.setCreateTime(now);
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.insert(trainCarriage);
        } else {
            trainCarriage.setUpdateTime(now);
            trainCarriageMapper.updateByPrimaryKey(trainCarriage);
        }
    }

    public PageResp<TrainCarriageQueryResp> queryTrainCarriageList(TrainCarriageQueryReq req) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        trainCarriageExample.setOrderByClause("id desc");

        LOG.info("查询页码：{}", req.getPageNum());
        LOG.info("每页条数：{}", req.getPageSize());
        PageHelper.startPage(req.getPageNum(), req.getPageSize());
        List<TrainCarriage> trainCarriageList = trainCarriageMapper.selectByExample(trainCarriageExample);

        PageInfo<TrainCarriage> pageInfo = new PageInfo<>(trainCarriageList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        PageResp<TrainCarriageQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(BeanUtil.copyToList(trainCarriageList, TrainCarriageQueryResp.class));
        return pageResp;
    }

    public void deleteTrainCarriage(Long id) {
        trainCarriageMapper.deleteByPrimaryKey(id);
    }

    public List<TrainCarriage> selectCarrigesByTrainCode(String trainCode) {
        TrainCarriageExample trainCarriageExample = new TrainCarriageExample();
        TrainCarriageExample.Criteria criteria1 = trainCarriageExample.createCriteria();
        criteria1.andTrainCodeEqualTo(trainCode);
        List<TrainCarriage> trainCarriages = trainCarriageMapper.selectByExample(trainCarriageExample);
        return trainCarriages;
    }
}
