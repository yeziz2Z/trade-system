package com.liuk.trade.pay.service;

import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;

/**
 * Created by kl on 2018/2/22.
 */
public interface IPayService {
    CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq);

    CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq);
}
