package com.liuk.trade.common.protocol.order;

import com.liuk.trade.common.protocol.BaseRes;

/**
 * Created by kl on 2018/2/18.
 */
public class ConfirmOrderRes extends BaseRes {

    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
