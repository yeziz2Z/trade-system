package com.liuk.trade.goods.service.impl;

import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.goods.*;
import com.liuk.trade.entity.TradeGoods;
import com.liuk.trade.entity.TradeGoodsNumberLog;
import com.liuk.trade.entity.TradeGoodsNumberLogKey;
import com.liuk.trade.goods.service.IGoodsService;
import com.liuk.trade.mapper.TradeGoodsMapper;
import com.liuk.trade.mapper.TradeGoodsNumberLogMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by kl on 2018/2/22.
 */
@Service
public class GoodsServiceImpl implements IGoodsService{

    @Autowired
    private TradeGoodsMapper tradeGoodsMapper;
    @Autowired
    private TradeGoodsNumberLogMapper tradeGoodsNumberLogMapper;


    public QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq) {
        QueryGoodsRes queryGoodsRes = new QueryGoodsRes();
        queryGoodsRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        queryGoodsRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getCode());
        try {
            if(queryGoodsReq == null || queryGoodsReq.getGoodsId() == null){
                throw new Exception("查询商品信息ID不正确！");
            }
            TradeGoods tradeGoods = tradeGoodsMapper.selectByPrimaryKey(queryGoodsReq.getGoodsId());
            if (tradeGoods != null){
                BeanUtils.copyProperties(tradeGoods,queryGoodsRes);
            }else {
                throw new Exception("未查询到该商品！");
            }
        }catch (Exception e){
            queryGoodsRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            queryGoodsRes.setRetInfo(e.getMessage());
        }
        return queryGoodsRes;
    }

    @Transactional
    public ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq) {
        ReduceGoodsNumberRes reduceGoodsNumberRes = new ReduceGoodsNumberRes();
        reduceGoodsNumberRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        reduceGoodsNumberRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getCode());
        if(reduceGoodsNumberReq == null || reduceGoodsNumberReq.getGoodsId() == null || reduceGoodsNumberReq.getGoodsNumber() == null
                || reduceGoodsNumberReq.getGoodsNumber() <= 0){
            throw new RuntimeException("扣减库存请求参数不正确！");
        }
        TradeGoods tradeGoods = new TradeGoods();
        tradeGoods.setGoodsId(reduceGoodsNumberReq.getGoodsId());
        tradeGoods.setGoodsNumber(reduceGoodsNumberReq.getGoodsNumber());
        int i = tradeGoodsMapper.reduceTradeGoodsNumber(tradeGoods);
        if (i <= 0){
            throw new RuntimeException("扣减库存失败！");
        }
        TradeGoodsNumberLog tradeGoodsNumberLog = new TradeGoodsNumberLog();
        tradeGoodsNumberLog.setGoodsNumber(reduceGoodsNumberReq.getGoodsNumber());
        tradeGoodsNumberLog.setGoodsId(reduceGoodsNumberReq.getGoodsId());
        tradeGoodsNumberLog.setOrderId(reduceGoodsNumberReq.getOrderId());
        tradeGoodsNumberLog.setLogTime(new Date());
        tradeGoodsNumberLogMapper.insert(tradeGoodsNumberLog);
        return reduceGoodsNumberRes;
    }

    @Transactional
    public AddGoodsNumberRes addGoodsNumber(AddGoodsNumberReq addGoodsNumberReq) {
        AddGoodsNumberRes addGoodsNumberRes = new AddGoodsNumberRes();
        addGoodsNumberRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        addGoodsNumberRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getCode());
        try {
            if(addGoodsNumberReq == null || addGoodsNumberReq.getGoodsId() == null || addGoodsNumberReq.getGoodsNumber() == null
                    || addGoodsNumberReq.getGoodsNumber() <= 0){
                throw new RuntimeException("增加库存请求参数不正确！");
            }
            if(addGoodsNumberReq.getGoodsId() != null){
                TradeGoodsNumberLogKey tradeGoodsNumberLogKey = new TradeGoodsNumberLogKey();
                tradeGoodsNumberLogKey.setGoodsId(addGoodsNumberReq.getGoodsId());
                tradeGoodsNumberLogKey.setOrderId(addGoodsNumberReq.getOrderId());
                TradeGoodsNumberLog tradeGoodsNumberLog = tradeGoodsNumberLogMapper.selectByPrimaryKey(tradeGoodsNumberLogKey);
                if (tradeGoodsNumberLog == null){
                    throw new Exception("未找到扣库存记录！");
                }
                TradeGoods tradeGoods = new TradeGoods();
                tradeGoods.setGoodsId(addGoodsNumberReq.getGoodsId());
                tradeGoods.setGoodsNumber(addGoodsNumberReq.getGoodsNumber());
                int i = tradeGoodsMapper.addTradeGoodsNumber(tradeGoods);
                if (i<=0){
                    throw new Exception("增加库存失败！");
                }
            }
        }catch (Exception e){
            addGoodsNumberRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            addGoodsNumberRes.setRetInfo(e.getMessage());
        }
        return addGoodsNumberRes;
    }
}
