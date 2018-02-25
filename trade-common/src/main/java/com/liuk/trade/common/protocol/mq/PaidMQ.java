package com.liuk.trade.common.protocol.mq;

import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/24.
 */
public class PaidMQ {
    private String payId;
    private String orderId;
    private BigDecimal payAmount;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
}
