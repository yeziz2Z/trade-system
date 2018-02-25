import com.alibaba.fastjson.JSON;
import com.liuk.trade.common.api.*;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.coupon.QueryCouponReq;
import com.liuk.trade.common.protocol.coupon.QueryCouponRes;
import com.liuk.trade.common.protocol.goods.QueryGoodsReq;
import com.liuk.trade.common.protocol.goods.QueryGoodsRes;
import com.liuk.trade.common.protocol.order.ConfirmOrderReq;
import com.liuk.trade.common.protocol.order.ConfirmOrderRes;
import com.liuk.trade.common.protocol.pay.CallbackPaymentReq;
import com.liuk.trade.common.protocol.pay.CallbackPaymentRes;
import com.liuk.trade.common.protocol.pay.CreatePaymentReq;
import com.liuk.trade.common.protocol.pay.CreatePaymentRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

/**
 * Created by kl on 2018/2/24.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:xml/spring-rest-client.xml")
public class TestTrade {

    @Autowired
    private IGoodsApi goodsApi;
    @Autowired
    private IPayApi payApi;
    @Autowired
    private ICouponApi couponApi;
    @Autowired
    private IUserApi userApi;
    @Autowired
    private IOrderApi orderApi;

    @Test
    public void testUser(){
        QueryUserReq queryUserReq = new QueryUserReq();
        queryUserReq.setUserId(1);
        QueryUserRes queryUserRes = userApi.queryUserById(queryUserReq);
        System.out.println(JSON.toJSONString(queryUserRes));
    }

    @Test
    public void testOrder(){

    }
    @Test
    public void testGoods(){
        QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
        queryGoodsReq.setGoodsId(10000);
        QueryGoodsRes queryGoodsRes = goodsApi.queryGoods(queryGoodsReq);
        System.out.println(JSON.toJSONString(queryGoodsRes));
    }

    @Test
    public void testCoupon(){
        QueryCouponReq queryCouponReq = new QueryCouponReq();
        queryCouponReq.setCouponId("123456789");
        QueryCouponRes queryCouponRes = couponApi.queryCoupon(queryCouponReq);
        System.out.println(JSON.toJSONString(queryCouponRes));
    }

    @Test
    public void testConfirmOrder(){
        ConfirmOrderReq confirmOrderReq = new ConfirmOrderReq();
        confirmOrderReq.setGoodsId(10000);
        confirmOrderReq.setUserId(2);
        confirmOrderReq.setGoodsNumber(1);
        confirmOrderReq.setAddress("上海");
        confirmOrderReq.setGoodsPrice(new BigDecimal("5000"));
        confirmOrderReq.setOrderAmount(new BigDecimal("5000"));
        confirmOrderReq.setMoneyPaid(new BigDecimal("100"));
        confirmOrderReq.setCouponId("987654321");
        ConfirmOrderRes confirmOrderRes = orderApi.confirmOrder(confirmOrderReq);
        System.out.println(JSON.toJSONString(confirmOrderRes));
    }
    @Test
    public void testCreatePayment(){
        CreatePaymentReq createPaymentReq = new CreatePaymentReq();
        createPaymentReq.setOrderId("ba4ea7769fe74fe6a1190910b5cb2878");
        createPaymentReq.setPayAmount(new BigDecimal("4800"));
        CreatePaymentRes payment = payApi.createPayment(createPaymentReq);
        System.out.println(JSON.toJSONString(payment));
    }

    @Test
    public void testCallbackPayment(){
        CallbackPaymentReq callbackPaymentReq = new CallbackPaymentReq();
        callbackPaymentReq.setIsPaid(TradeEnums.YesNoEnum.YES.getCode());
        callbackPaymentReq.setPayId("2a3091b14493463682da362096f09c09");
        CallbackPaymentRes callbackPaymentRes = payApi.callbackPayment(callbackPaymentReq);
        System.out.println(JSON.toJSONString(callbackPaymentRes));
    }
}
