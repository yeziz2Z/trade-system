package com.liuk.trade.common.protocol.goods;

/**
 * Created by kl on 2018/2/22.
 */
public class AddGoodsNumberReq {
    private Integer goodsId;
    private Integer goodsNumber;
    private String orderId;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
