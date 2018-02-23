package com.liuk.trade.order.mq.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.mq.CancelOrderMQ;
import com.liuk.trade.common.rocketmq.IMessageProcessor;
import com.liuk.trade.entity.TradeOrder;
import com.liuk.trade.mapper.TradeOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/19.
 */
public class CancelOrderProcessor implements IMessageProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(CancelOrderProcessor.class);
    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            LOGGER.info("user CancelOrderProcessor receive message :" + messageExt);
            if(cancelOrderMQ !=null && cancelOrderMQ.getUserMoney().compareTo(BigDecimal.ZERO) == 1){
                TradeOrder tradeOrder = new TradeOrder();
                tradeOrder.setOrderId(cancelOrderMQ.getOrderId());
                tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.CANCEL.getStatusCode());
                tradeOrderMapper.updateByPrimaryKey(tradeOrder);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
