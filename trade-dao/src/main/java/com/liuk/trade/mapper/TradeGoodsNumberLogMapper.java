package com.liuk.trade.mapper;

import com.liuk.trade.entity.TradeGoodsNumberLog;
import com.liuk.trade.entity.TradeGoodsNumberLogExample;
import com.liuk.trade.entity.TradeGoodsNumberLogKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeGoodsNumberLogMapper {
    long countByExample(TradeGoodsNumberLogExample example);

    int deleteByExample(TradeGoodsNumberLogExample example);

    int deleteByPrimaryKey(TradeGoodsNumberLogKey key);

    int insert(TradeGoodsNumberLog record);

    int insertSelective(TradeGoodsNumberLog record);

    List<TradeGoodsNumberLog> selectByExample(TradeGoodsNumberLogExample example);

    TradeGoodsNumberLog selectByPrimaryKey(TradeGoodsNumberLogKey tradeGoodsNumberLogKey);

    int updateByExampleSelective(@Param("record") TradeGoodsNumberLog record, @Param("example") TradeGoodsNumberLogExample example);

    int updateByExample(@Param("record") TradeGoodsNumberLog record, @Param("example") TradeGoodsNumberLogExample example);

    int updateByPrimaryKeySelective(TradeGoodsNumberLog record);

    int updateByPrimaryKey(TradeGoodsNumberLog record);

//    TradeGoodsNumberLog selectByPrimaryKey2(TradeGoodsNumberLogKey tradeGoodsNumberLogKey);
}