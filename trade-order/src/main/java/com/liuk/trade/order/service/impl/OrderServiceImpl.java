package com.liuk.trade.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.liuk.trade.common.api.ICouponApi;
import com.liuk.trade.common.api.IGoodsApi;
import com.liuk.trade.common.api.IUserApi;
import com.liuk.trade.common.constants.MQEnums;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.exception.LiukMQException;
import com.liuk.trade.common.exception.LiukOrderException;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;
import com.liuk.trade.common.protocol.goods.QueryGoodsReq;
import com.liuk.trade.common.protocol.goods.QueryGoodsRes;
import com.liuk.trade.common.protocol.goods.ReduceGoodsNumberReq;
import com.liuk.trade.common.protocol.goods.ReduceGoodsNumberRes;
import com.liuk.trade.common.protocol.mq.CancelOrderMQ;
import com.liuk.trade.common.protocol.order.ConfirmOrderReq;
import com.liuk.trade.common.protocol.order.ConfirmOrderRes;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyReq;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import com.liuk.trade.common.rocketmq.LiukMQProducer;
import com.liuk.trade.common.util.IDGenerator;
import com.liuk.trade.entity.TradeOrder;
import com.liuk.trade.mapper.TradeOrderMapper;
import com.liuk.trade.order.service.IOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kl on 2018/2/18.
 */
@Service
public class OrderServiceImpl implements IOrderService{


    @Autowired
    private IGoodsApi goodsApi;
    @Autowired
    private ICouponApi couponApi;
    @Autowired
    private IUserApi userApi;
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Autowired
    private LiukMQProducer producer;

    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq) {
        ConfirmOrderRes confirmOrderRes = new ConfirmOrderRes();
        confirmOrderRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        try {
            QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
            queryGoodsReq.setGoodsId(confirmOrderReq.getGoodsId());
            QueryGoodsRes queryGoodsRes = goodsApi.queryGoods(queryGoodsReq);
            //1.检测校验
            checkConfirmOrderReq(confirmOrderReq,queryGoodsRes);
            //2.创建不可见订单
            String orderId = saveNoConfirmOrder(confirmOrderReq);
            //3.调用远程服务，扣优惠券、扣库存、扣余额，如果调用成功 ->更改订单状态可见  失败->发送MQ消息 ，进行取消订单
            callRemoteService(orderId,confirmOrderReq);
            confirmOrderRes.setOrderId(orderId);
        } catch (Exception e) {
            confirmOrderRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            confirmOrderRes.setRetInfo(e.getMessage());
        }
        return confirmOrderRes;
    }
    //调用远程服务，扣优惠券、扣库存、扣余额，如果调用成功 ->更改订单状态可见  失败->发送MQ消息 ，进行取消订单
    private void callRemoteService(String orderId,ConfirmOrderReq confirmOrderReq){
        try {
            //调用优惠券
            if(StringUtils.isNotBlank(confirmOrderReq.getCouponId())){
                ChangeCouponStatusReq changeCouponStatusReq = new ChangeCouponStatusReq();
                changeCouponStatusReq.setCouponId(confirmOrderReq.getCouponId());
                changeCouponStatusReq.setIsUsed(TradeEnums.YesNoEnum.YES.getCode());
                changeCouponStatusReq.setOrderId(orderId);
                ChangeCouponStatusRes changeCouponStatusRes = couponApi.changeCouponStatus(changeCouponStatusReq);
                if (!changeCouponStatusRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
                    throw new Exception("优惠券使用失败");
                }
            }
            //扣余额
            if(confirmOrderReq.getMoneyPaid() != null &&confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO) == 1){
                ChangeUserMoneyReq changeUserMoneyReq = new ChangeUserMoneyReq();
                changeUserMoneyReq.setOrderId(orderId);
                changeUserMoneyReq.setUserId(confirmOrderReq.getUserId());
                changeUserMoneyReq.setUserMoney(confirmOrderReq.getMoneyPaid());
                changeUserMoneyReq.setMoneyLogType(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode());
                ChangeUserMoneyRes changeUserMoneyRes = userApi.changeUserMoney(changeUserMoneyReq);
                if (!changeUserMoneyRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
                    throw new Exception("扣用户余额失败");
                }
            }
            //扣库存
            ReduceGoodsNumberReq reduceGoodsNumberReq = new ReduceGoodsNumberReq();
            reduceGoodsNumberReq.setGoodsId(confirmOrderReq.getGoodsId());
            reduceGoodsNumberReq.setGoodsNumber(confirmOrderReq.getGoodsNumber());
            reduceGoodsNumberReq.setOrderId(orderId);
            ReduceGoodsNumberRes reduceGoodsNumberRes = goodsApi.reduceGoodsNumber(reduceGoodsNumberReq);
            if(!reduceGoodsNumberRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
                throw new Exception("扣库存失败");
            }
            /*if(1 == 1){
                throw new Exception("手动异常，emnmnmn。。。");
            }*/
            //更改订单状态
            TradeOrder tradeOrder = new TradeOrder();
            tradeOrder.setOrderId(orderId);
            tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.CONFIRM.getStatusCode());
            tradeOrder.setConfirmTime(new Date());
            int ret = tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
            if (ret <= 0){
                throw new Exception("修改订单状态失败");
            }
        }catch (Exception e){
            //发送MQ消息
            CancelOrderMQ cancelOrderMQ = new CancelOrderMQ();
            cancelOrderMQ.setOrderId(orderId);
            cancelOrderMQ.setUserId(confirmOrderReq.getUserId());
            cancelOrderMQ.setGoodsId(confirmOrderReq.getGoodsId());
            cancelOrderMQ.setGoodsNumber(confirmOrderReq.getGoodsNumber());
            cancelOrderMQ.setCouponId(confirmOrderReq.getCouponId());
            cancelOrderMQ.setUserMoney(confirmOrderReq.getMoneyPaid());
            try {
                SendResult sendResult = producer.sendMessage(MQEnums.TopicEnum.ORDER_CANCEL, orderId, JSON.toJSONString(cancelOrderMQ));
                System.out.println(sendResult);
            } catch (LiukMQException e1) {
            }
            throw new RuntimeException(e.getMessage());
        }
    }
    private String saveNoConfirmOrder(ConfirmOrderReq confirmOrderReq) throws Exception{
        TradeOrder tradeOrder = new TradeOrder();
        String orderId = IDGenerator.generateUUID();
        tradeOrder.setOrderId(orderId);
        tradeOrder.setUserId(confirmOrderReq.getUserId());
        tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.NO_CONFIRM.getStatusCode());
        tradeOrder.setPayStatus(TradeEnums.PayStatusEnum.NO_PAY.getStatusCode());
        tradeOrder.setShoppingStatus(TradeEnums.ShippingStatusEnum.NO_SHIP.getStatusCode());
        tradeOrder.setAddress(confirmOrderReq.getAddress());
        tradeOrder.setConsignee(confirmOrderReq.getConsignee());
        tradeOrder.setGoodsId(confirmOrderReq.getGoodsId());
        tradeOrder.setGoodsNumber(confirmOrderReq.getGoodsNumber());
        tradeOrder.setGoodsPrice(confirmOrderReq.getGoodsPrice());
        BigDecimal goodsAmount = confirmOrderReq.getGoodsPrice().multiply(new BigDecimal(confirmOrderReq.getGoodsNumber()));
        tradeOrder.setGoodsAmount(goodsAmount);
        BigDecimal shippingFee = calulateShippingFee(goodsAmount);
        if(confirmOrderReq.getShippingFee().compareTo(shippingFee) != 0 ){
            throw new Exception("快递费用不正确！");
        }
        tradeOrder.setShippingFee(shippingFee);
        BigDecimal orderAmount = goodsAmount.add(shippingFee);
        if(confirmOrderReq.getOrderAmount().compareTo(orderAmount) != 0 ){
            throw new Exception("订单总价异常，请重新下单！");
        }
        tradeOrder.setOrderAmount(orderAmount);

        String couponId = confirmOrderReq.getCouponId();
        //优惠券不为空
        if(StringUtils.isNoneBlank(couponId)){
            //查询优惠券状态
            QueryCouponReq queryCouponReq = new QueryCouponReq();
            queryCouponReq.setCouponId(couponId);
            QueryCouponRes queryCouponRes = couponApi.queryCoupon(queryCouponReq);
            if(queryCouponRes == null || !queryCouponRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
                throw new Exception("优惠券非法");
            }
            if (!queryCouponRes.getIsUsed().equals(TradeEnums.YesNoEnum.NO.getCode())){
                throw new Exception("优惠券已使用");
            }
            tradeOrder.setCouponId(couponId);
            tradeOrder.setCouponPaid(queryCouponRes.getCouponAmount());
        }else {
            tradeOrder.setCouponPaid(BigDecimal.ZERO);
        }

        //余额支付
        if(confirmOrderReq.getMoneyPaid() != null){
            int r = confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO);
            if (r == -1){
                throw new Exception("余额金额非法");
            }
            if(r == 1){
                //判断当前账户余额
                QueryUserReq queryUserReq = new QueryUserReq();
                queryUserReq.setUserId(confirmOrderReq.getUserId());
                QueryUserRes queryUserRes = userApi.queryUserById(queryUserReq);
                if (queryUserRes == null || !queryUserRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
                    throw new LiukOrderException("用户非法！");
                }
                if(queryUserRes.getUserMoney().compareTo(confirmOrderReq.getMoneyPaid()) == -1){
                    throw new LiukOrderException("余额不足！");
                }
                tradeOrder.setMoneyPaid(confirmOrderReq.getMoneyPaid());
            }
        }else {
            tradeOrder.setMoneyPaid(BigDecimal.ZERO);
        }
        BigDecimal payAmount = orderAmount.subtract(tradeOrder.getMoneyPaid()).subtract(tradeOrder.getCouponPaid());
        tradeOrder.setPayAmount(payAmount);
        tradeOrder.setAddTime(new Date());

        int ret = this.tradeOrderMapper.insert(tradeOrder);
        if (ret != 1){
            throw new LiukOrderException("保存订单失败！");
        }
        return orderId;
    }

    private void checkConfirmOrderReq(ConfirmOrderReq confirmOrderReq,QueryGoodsRes queryGoodsRes){
        if (confirmOrderReq == null){
            throw new LiukOrderException("下单信息不能为空！");
        }
        if (confirmOrderReq.getUserId() == null){
            throw new LiukOrderException("会员帐号不能为空！");
        }
        if (confirmOrderReq.getGoodsId() == null){
            throw new LiukOrderException("商品编号不能为空！");
        }
        if (confirmOrderReq.getGoodsNumber() == null || confirmOrderReq.getGoodsNumber()<0){
            throw new LiukOrderException("购买数量不能小于0！");
        }
        if (confirmOrderReq.getAddress()==null){
            throw new LiukOrderException("收货地址不能为空！");
        }
        if (queryGoodsRes == null || !queryGoodsRes.getRetCode().equals(TradeEnums.ResEnum.SUCCESS.getCode())){
            throw new LiukOrderException("未查询到该商品["+confirmOrderReq.getGoodsId()+"]");
        }
        if (queryGoodsRes.getGoodsNumber() < confirmOrderReq.getGoodsNumber()){
            throw new LiukOrderException("商品库存不足！");
        }
        if(queryGoodsRes.getGoodsPrice().compareTo(confirmOrderReq.getGoodsPrice()) != 0){
            throw new LiukOrderException("当前商品价格有变化，请重新下单！");
        }
        if(confirmOrderReq.getShippingFee() == null){
            confirmOrderReq.setShippingFee(BigDecimal.ZERO);
        }
        if (confirmOrderReq.getOrderAmount() == null){
            confirmOrderReq.setOrderAmount(BigDecimal.ZERO);
        }
    }

    private BigDecimal calulateShippingFee(BigDecimal goodsAmount){
        if (goodsAmount.doubleValue()>100){
            return BigDecimal.ZERO;
        }else {
            return new BigDecimal("10");
        }
    }
}
