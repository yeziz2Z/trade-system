package com.liuk.trade.common.protocol.coupon;

/**
 * Created by kl on 2018/2/18.
 */
public class ChangeCouponStatusReq {

    private String couponId;
    private String orderId;
    private String isUsed;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed;
    }
}
