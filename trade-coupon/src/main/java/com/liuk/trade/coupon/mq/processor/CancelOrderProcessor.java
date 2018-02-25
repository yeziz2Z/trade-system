package com.liuk.trade.coupon.mq.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.mq.CancelOrderMQ;
import com.liuk.trade.common.rocketmq.IMessageProcessor;
import com.liuk.trade.coupon.service.ICouponService;
import com.liuk.trade.entity.TradeOrder;
import com.liuk.trade.mapper.TradeOrderMapper;
import org.apache.commons.lang3.StringUtils;
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
    private ICouponService couponService;
    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body = new String(messageExt.getBody(),"UTF-8");
            String msgId = messageExt.getMsgId();
            String tags = messageExt.getTags();
            String keys = messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor receive message :" + messageExt);
            CancelOrderMQ cancelOrderMQ = JSON.parseObject(body,CancelOrderMQ.class);
            if(StringUtils.isNotBlank(cancelOrderMQ.getCouponId())){
                ChangeCouponStatusReq changeCouponStatusReq = new ChangeCouponStatusReq();
                changeCouponStatusReq.setCouponId(cancelOrderMQ.getCouponId());
                changeCouponStatusReq.setOrderId(cancelOrderMQ.getOrderId());
                changeCouponStatusReq.setIsUsed(TradeEnums.YesNoEnum.NO.getCode());
                ChangeCouponStatusRes changeCouponStatusRes = couponService.changeCouponStatus(changeCouponStatusReq);
                System.out.println("优惠券Mq回滚："+JSON.toJSONString(changeCouponStatusRes));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
