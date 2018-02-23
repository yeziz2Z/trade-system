package com.liuk.trade.order.service;

import com.liuk.trade.common.protocol.order.ConfirmOrderReq;
import com.liuk.trade.common.protocol.order.ConfirmOrderRes;

/**
 * Created by kl on 2018/2/18.
 */
public interface IOrderService {
    ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq);
}
