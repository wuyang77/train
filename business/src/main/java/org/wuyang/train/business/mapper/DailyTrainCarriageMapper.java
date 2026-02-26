package org.wuyang.train.business.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.wuyang.train.business.domain.DailyTrainCarriage;
import org.wuyang.train.business.domain.DailyTrainCarriageExample;

public interface DailyTrainCarriageMapper {
    long countByExample(DailyTrainCarriageExample example);

    int deleteByExample(DailyTrainCarriageExample example);

    int deleteByPrimaryKey(Long id);

    int insert(DailyTrainCarriage record);

    int insertSelective(DailyTrainCarriage record);

    List<DailyTrainCarriage> selectByExample(DailyTrainCarriageExample example);

    DailyTrainCarriage selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") DailyTrainCarriage record, @Param("example") DailyTrainCarriageExample example);

    int updateByExample(@Param("record") DailyTrainCarriage record, @Param("example") DailyTrainCarriageExample example);

    int updateByPrimaryKeySelective(DailyTrainCarriage record);

    int updateByPrimaryKey(DailyTrainCarriage record);
}