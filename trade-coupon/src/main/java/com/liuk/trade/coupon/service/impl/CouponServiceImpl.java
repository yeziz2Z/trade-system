package com.liuk.trade.coupon.service.impl;

import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;
import com.liuk.trade.coupon.service.ICouponService;
import com.liuk.trade.entity.TradeCoupon;
import com.liuk.trade.mapper.TradeCouponMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kl on 2018/2/20.
 */
@Service
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private TradeCouponMapper tradeCouponMapper;

    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq) {
        QueryCouponRes queryCouponRes = new QueryCouponRes();
        queryCouponRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        queryCouponRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());

        try {
            if (queryCouponReq == null || StringUtils.isBlank(queryCouponReq.getCouponId())){
                throw new Exception("请求参数不正确，优惠券编号为空！");
            }
            TradeCoupon tradeCoupon = tradeCouponMapper.selectByPrimaryKey(queryCouponReq.getCouponId());
            if (tradeCoupon != null){
                BeanUtils.copyProperties(tradeCoupon,queryCouponRes);
            }else {
                throw new Exception("找不到该优惠券！");
            }
        }catch (Exception e){
            queryCouponRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            queryCouponRes.setRetInfo(e.getMessage());
        }
        return queryCouponRes;
    }


    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq) {
        ChangeCouponStatusRes changeCouponStatusRes = new ChangeCouponStatusRes();
        changeCouponStatusRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        changeCouponStatusRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());
        try {
            if (changeCouponStatusReq == null || StringUtils.isBlank(changeCouponStatusReq.getCouponId())){
                throw new Exception("请求参数不正确，优惠券编号为空！");
            }
            //使用优惠券
            TradeCoupon tradeCoupon = new TradeCoupon();
            tradeCoupon.setCouponId(changeCouponStatusReq.getCouponId());
            tradeCoupon.setOrderId(changeCouponStatusReq.getOrderId());
            if(changeCouponStatusReq.getIsUsed().equals(TradeEnums.YesNoEnum.YES.getCode())){
                int i = tradeCouponMapper.useCoupon(tradeCoupon);
                if(i<=0){
                    throw new Exception("使用优惠券失败！");
                }
            }else if(changeCouponStatusReq.getIsUsed().equals(TradeEnums.YesNoEnum.NO.getCode())){
                tradeCouponMapper.unUseCoupon(tradeCoupon);
            }
        }catch (Exception e){
            changeCouponStatusRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            changeCouponStatusRes.setRetInfo(e.getMessage());
        }
        return changeCouponStatusRes;
    }
}
