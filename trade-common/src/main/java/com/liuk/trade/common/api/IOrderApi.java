package com.liuk.trade.common.api;

import com.liuk.trade.common.protocol.order.ConfirmOrderReq;
import com.liuk.trade.common.protocol.order.ConfirmOrderRes;

/**
 * Created by kl on 2018/2/18.
 */
public interface IOrderApi {
    ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq);


}
