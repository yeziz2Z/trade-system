package com.liuk.trade.order.mq.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.mq.PaidMQ;
import com.liuk.trade.common.rocketmq.IMessageProcessor;
import com.liuk.trade.entity.TradeOrder;
import com.liuk.trade.mapper.TradeOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by kl on 2018/2/24.
 */
public class PaidProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    public boolean handleMessage(MessageExt messageExt) {
        System.out.println("21313");
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            PaidMQ paidMQ = JSON.parseObject(body,PaidMQ.class);

            TradeOrder tradeOrder = new TradeOrder();
            tradeOrder.setOrderId(paidMQ.getOrderId());
            tradeOrder.setPayStatus(TradeEnums.PayStatusEnum.PAID.getStatusCode());
            tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
            return true;
        }catch (Exception e){

            return false;
        }
    }
}
