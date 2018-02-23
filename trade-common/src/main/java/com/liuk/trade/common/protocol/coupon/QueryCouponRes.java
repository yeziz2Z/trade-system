package com.liuk.trade.common.protocol.coupon;

import com.liuk.trade.common.protocol.BaseRes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kl on 2018/2/18.
 */
public class QueryCouponRes extends BaseRes{
    private String couponId;

    private BigDecimal couponAmount;

    private Integer userId;

    private String orderId;

    private String isUsed;

    private Date usedTime;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(BigDecimal couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }
}
