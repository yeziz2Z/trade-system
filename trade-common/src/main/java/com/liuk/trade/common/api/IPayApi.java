package com.liuk.trade.common.api;

import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;

/**
 * Created by kl on 2018/2/18.
 */
public interface IPayApi {
    CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq);

    CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq);
}
