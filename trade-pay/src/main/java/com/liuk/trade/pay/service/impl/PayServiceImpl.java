package com.liuk.trade.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.liuk.trade.common.constants.MQEnums;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.exception.LiukMQException;
import com.liuk.trade.common.protocol.mq.PaidMQ;
import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;
import com.liuk.trade.common.rocketmq.LiukMQProducer;
import com.liuk.trade.common.util.IDGenerator;
import com.liuk.trade.entity.TradeMqProducerTemp;
import com.liuk.trade.entity.TradePay;
import com.liuk.trade.entity.TradePayExample;
import com.liuk.trade.mapper.TradeMqProducerTempMapper;
import com.liuk.trade.mapper.TradePayMapper;
import com.liuk.trade.pay.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kl on 2018/2/22.
 */
@Service
public class PayServiceImpl implements IPayService {

    @Autowired
    private TradePayMapper tradePayMapper;

    @Autowired
    private TradeMqProducerTempMapper tradeMqProducerTempMapper;

    @Autowired
    private LiukMQProducer liukMQProducer;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq) {
        CreatePaymentRes createPaymentRes = new CreatePaymentRes();
        createPaymentRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        createPaymentRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());
        try{
            TradePayExample tradePayExample = new TradePayExample();
            tradePayExample.createCriteria()
                    .andOrderIdEqualTo(createPaymentReq.getOrderId())
                    .andIsPaidEqualTo(TradeEnums.YesNoEnum.YES.getCode());
            int i = tradePayMapper.countByExample(tradePayExample);
            if (i > 0){
                throw new Exception("订单已支付！");
            }
            String payId = IDGenerator.generateUUID();
            TradePay tradePay = new TradePay();
            tradePay.setPayId(payId);
            tradePay.setOrderId(createPaymentReq.getOrderId());
            tradePay.setPayAmount(createPaymentReq.getPayAmount());
            tradePay.setIsPaid(TradeEnums.YesNoEnum.NO.getCode());
            tradePayMapper.insert(tradePay);
            System.out.println("创建支付订单成功");
        }catch (Exception e){
            createPaymentRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            createPaymentRes.setRetInfo("创建订单失败：" + e.getMessage());
        }
        return createPaymentRes;
    }

    @Transactional
    public CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq) {
        CallbackPaymentRes callbackPaymentRes = new CallbackPaymentRes();
        callbackPaymentRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        callbackPaymentRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());
        if (callbackPaymentReq.getIsPaid().equals(TradeEnums.YesNoEnum.YES.getCode())){
            //更新支付状态
            TradePay tradePay = tradePayMapper.selectByPrimaryKey(callbackPaymentReq.getPayId());
            if (tradePay == null){
                throw new RuntimeException("未找到该订单！");
            }
            if(tradePay.getIsPaid().equals(TradeEnums.YesNoEnum.YES.getCode())){
                throw new RuntimeException("该支付单已经支付！");
            }
            tradePay.setIsPaid(TradeEnums.YesNoEnum.YES.getCode());
            int i = tradePayMapper.updateByPrimaryKey(tradePay);
            //发送可靠消息
            if (i == 1){
                final PaidMQ paidMQ = new PaidMQ();
                paidMQ.setOrderId(tradePay.getOrderId());
                paidMQ.setPayAmount(tradePay.getPayAmount());
                paidMQ.setPayId(tradePay.getPayId());

                final TradeMqProducerTemp tradeMqProducerTemp = new TradeMqProducerTemp();
                tradeMqProducerTemp.setId(IDGenerator.generateUUID());
                tradeMqProducerTemp.setGroupName("payProducerGroup");
                tradeMqProducerTemp.setMsgTag(MQEnums.TopicEnum.PAID.getTag());
                tradeMqProducerTemp.setMsgKeys(tradePay.getPayId());
                tradeMqProducerTemp.setMsgBody(JSON.toJSONString(paidMQ));
                tradeMqProducerTemp.setCreateTime(new Date());

                tradeMqProducerTempMapper.insert(tradeMqProducerTemp);

                //异步发送mq,发送成功清空发送表
                executorService.submit(new Runnable() {
                    public void run() {
                        try {
                            SendResult sendResult = liukMQProducer.sendMessage(MQEnums.TopicEnum.PAID, paidMQ.getPayId(), JSON.toJSONString(paidMQ));
                            System.out.println(sendResult);
                            if (sendResult.getSendStatus().equals(SendStatus.SEND_OK)){
                                tradeMqProducerTempMapper.deleteByPrimaryKey(tradeMqProducerTemp.getId());
                                System.out.println("删除消息成功");
                            }
                        }catch (LiukMQException e){
                            e.printStackTrace();
                        }
                    }
                });
            }else {
                throw new RuntimeException("该支付单已经支付！");
            }
        }
        return callbackPaymentRes;
    }
}
