package com.liuk.trade.common.protocol.pay;

import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/22.
 */
public class CreatePaymentReq {
    private String orderId;
    private BigDecimal payAmount;

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
