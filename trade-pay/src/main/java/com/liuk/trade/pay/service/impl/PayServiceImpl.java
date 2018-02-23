package com.liuk.trade.pay.service.impl;

import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;
import com.liuk.trade.pay.service.IPayService;
import org.springframework.stereotype.Service;

/**
 * Created by kl on 2018/2/22.
 */
@Service
public class PayServiceImpl implements IPayService {
    public CreatePaymentRes createPayment(CreatePaymentReq createPaymentReq) {
        return null;
    }

    public CallbackPaymentRes callbackPayment(CallbackPaymentReq callbackPaymentReq) {
        return null;
    }
}
