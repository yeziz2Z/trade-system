package com.liuk.trade.goods.mq.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.liuk.trade.common.constants.MQEnums;
import com.liuk.trade.common.protocol.goods.AddGoodsNumberReq;
import com.liuk.trade.common.protocol.mq.CancelOrderMQ;
import com.liuk.trade.common.rocketmq.IMessageProcessor;
import com.liuk.trade.entity.TradeMqConsumerLog;
import com.liuk.trade.entity.TradeMqConsumerLogExample;
import com.liuk.trade.entity.TradeMqConsumerLogKey;
import com.liuk.trade.goods.service.IGoodsService;
import com.liuk.trade.mapper.TradeMqConsumerLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/19.
 */
public class CancelOrderProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private TradeMqConsumerLogMapper tradeMqConsumerLogMapper;
    public boolean handleMessage(MessageExt messageExt) {
        TradeMqConsumerLog mqConsumerLog = null;
        try {
            String groupName = "goods_orderTopic_cancel_group";
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();

            LOGGER.info("user CancelOrderProcessor receive message :" + messageExt);
            TradeMqConsumerLogKey tradeMqConsumerLogKey = new TradeMqConsumerLogKey();
            tradeMqConsumerLogKey.setGroupName(groupName);
            tradeMqConsumerLogKey.setMsgKeys(keys);
            tradeMqConsumerLogKey.setMsgTag(tags);
            mqConsumerLog = tradeMqConsumerLogMapper.selectByPrimaryKey(tradeMqConsumerLogKey);
            if (mqConsumerLog != null){
                String consumerStatus = mqConsumerLog.getConsumerStatus();
                if (consumerStatus.equals(MQEnums.ConsumerStatusEnum.SUCCESS.getStatusCode())){
                    //返回成功 重复的处理消息
                    LOGGER.warn("已经处理过 不用在处理了");
                    return true;
                }else if(consumerStatus.equals(MQEnums.ConsumerStatusEnum.PROCESSING.getStatusCode())){
                    //返回失败 说明消费组内有正在处理消息 ，稍后重试
                    LOGGER.warn("正在处理中 稍后再试");
                    return false;
                }else {
                    if (mqConsumerLog.getConsumerTimes() >= 3){
                        //让这个消息不再重试 转人工处理
                        LOGGER.warn("超过3次，不再处理");
                        return true;
                    }
                    //更新处理状态
                    TradeMqConsumerLog updateConsumerLog = new TradeMqConsumerLog();
                    updateConsumerLog.setGroupName(mqConsumerLog.getGroupName());
                    updateConsumerLog.setMsgTag(mqConsumerLog.getMsgTag());
                    updateConsumerLog.setMsgKeys(mqConsumerLog.getMsgKeys());
                    updateConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.PROCESSING.getStatusCode());
                    //防止并发
                    TradeMqConsumerLogExample example = new TradeMqConsumerLogExample();
                    example.createCriteria().andGroupNameEqualTo(groupName)
                            .andMsgTagEqualTo(tags)
                            .andMsgKeysEqualTo(keys)
                            .andConsumerTimesEqualTo(mqConsumerLog.getConsumerTimes());
                    //乐观锁的方式防止并发更新
                    int i = tradeMqConsumerLogMapper.updateByExampleSelective(updateConsumerLog, example);
                    if(i <= 0){
                        LOGGER.warn("并发更新处理状态，一会重试！");
                        return false;
                    }
                }
            }else {
                //新插入去重表 并发时用主键冲突控制
                try {
                    mqConsumerLog = new TradeMqConsumerLog();
                    mqConsumerLog.setGroupName(groupName);
                    mqConsumerLog.setMsgKeys(keys);
                    mqConsumerLog.setMsgTag(tags);
                    mqConsumerLog.setMsgId(msgId);
                    mqConsumerLog.setMsgBody(body);
                    mqConsumerLog.setConsumerTimes(0);
                    mqConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.PROCESSING.getStatusCode());
                    tradeMqConsumerLogMapper.insert(mqConsumerLog);
                }catch (Exception e){
                    LOGGER.warn("主键冲突，说明有订阅者正在处理，稍后再试");
                    return false;
                }

            }
            //业务逻辑处理
            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            AddGoodsNumberReq addGoodsNumberReq = new AddGoodsNumberReq();
            addGoodsNumberReq.setGoodsId(cancelOrderMQ.getGoodsId());
            addGoodsNumberReq.setOrderId(cancelOrderMQ.getOrderId());
            addGoodsNumberReq.setGoodsNumber(cancelOrderMQ.getGoodsNumber());
            goodsService.addGoodsNumber(addGoodsNumberReq);

            //更新消息 处理成功
            TradeMqConsumerLog updateConsumerLog = new TradeMqConsumerLog();
            updateConsumerLog.setGroupName(groupName);
            updateConsumerLog.setMsgTag(tags);
            updateConsumerLog.setMsgKeys(keys);
            updateConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.SUCCESS.getStatusCode());
            updateConsumerLog.setConsumerTimes(mqConsumerLog.getConsumerTimes()+1);
            tradeMqConsumerLogMapper.updateByPrimaryKeySelective(updateConsumerLog);
            return true;
        } catch (Exception e) {
            //更新消息 处理失败
            TradeMqConsumerLog updateConsumerLog = new TradeMqConsumerLog();
            updateConsumerLog.setGroupName(mqConsumerLog.getGroupName());
            updateConsumerLog.setMsgTag(mqConsumerLog.getMsgTag());
            updateConsumerLog.setMsgKeys(mqConsumerLog.getMsgKeys());
            updateConsumerLog.setConsumerStatus(MQEnums.ConsumerStatusEnum.FAIL.getStatusCode());
            updateConsumerLog.setConsumerTimes(mqConsumerLog.getConsumerTimes()+1);
            tradeMqConsumerLogMapper.updateByPrimaryKeySelective(updateConsumerLog);
            return false;
        }
    }
}
