package com.liuk.trade.common.api;

import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;

/**
 * Created by kl on 2018/2/18.
 */
public interface ICouponApi {

    QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq);

    ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq);
}
