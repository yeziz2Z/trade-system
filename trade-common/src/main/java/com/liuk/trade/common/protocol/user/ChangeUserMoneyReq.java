package com.liuk.trade.common.protocol.user;

import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/18.
 */
public class ChangeUserMoneyReq {
    private Integer userId;
    private BigDecimal userMoney;
    private String moneyLogType;
    private String orderId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    public String getMoneyLogType() {
        return moneyLogType;
    }

    public void setMoneyLogType(String moneyLogType) {
        this.moneyLogType = moneyLogType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
