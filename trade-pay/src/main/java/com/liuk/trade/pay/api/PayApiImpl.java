package com.liuk.trade.pay.api;

import com.liuk.trade.common.api.IPayApi;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;
import com.liuk.trade.pay.service.IPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kl on 2018/2/22.
 */
@Controller
public class PayApiImpl implements IPayApi {

    @Autowired
    private IPayService payService;

    @RequestMapping(value = "/createPayment",method = RequestMethod.POST)
    @ResponseBody
    public CreatePaymentRes createPayment(@RequestBody CreatePaymentReq createPaymentReq) {
        return payService.createPayment(createPaymentReq);
    }

    @RequestMapping(value = "/callbackPayment",method = RequestMethod.POST)
    @ResponseBody
    public CallbackPaymentRes callbackPayment(@RequestBody CallbackPaymentReq callbackPaymentReq) {
        CallbackPaymentRes callbackPaymentRes = new CallbackPaymentRes();
        try {
            callbackPaymentRes = payService.callbackPayment(callbackPaymentReq);
        }catch (Exception e){
            callbackPaymentRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            callbackPaymentRes.setRetInfo(e.getMessage());
        }
        return callbackPaymentRes;
    }
}
