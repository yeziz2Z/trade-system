package com.liuk.trade.coupon.api;

import com.liuk.trade.common.api.ICouponApi;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.liuk.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;
import com.liuk.trade.coupon.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kl on 2018/2/20.
 */
@Controller
public class CouponApiImpl implements ICouponApi{

    @Autowired
    private ICouponService couponService;

    @RequestMapping(value = "/queryCoupon",method = RequestMethod.POST)
    @ResponseBody
    public QueryCouponRes queryCoupon(@RequestBody QueryCouponReq queryCouponReq) {
        return this.couponService.queryCoupon(queryCouponReq);
    }


    @RequestMapping(value = "/changeCouponStatus",method = RequestMethod.POST)
    @ResponseBody
    public ChangeCouponStatusRes changeCouponStatus(@RequestBody ChangeCouponStatusReq changeCouponStatusReq) {
        return this.couponService.changeCouponStatus(changeCouponStatusReq);
    }
}
