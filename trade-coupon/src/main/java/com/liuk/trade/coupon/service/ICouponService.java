package com.liuk.trade.coupon.service;

import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;

/**
 * Created by kl on 2018/2/20.
 */
public interface ICouponService {
    QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq);

    ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq);
}
