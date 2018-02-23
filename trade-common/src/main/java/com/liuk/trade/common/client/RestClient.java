package com.liuk.trade.common.client;

import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import org.springframework.web.client.RestTemplate;

/**
 * Created by kl on 2018/2/18.
 */
public class RestClient {
    private static RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        QueryUserReq req = new QueryUserReq();
        req.setUserId(2);
        QueryUserRes res = restTemplate.postForObject(TradeEnums.RestServerEnum.USER.getServerUrl()+"queryUserById", req, QueryUserRes.class);
        System.out.println(res);
    }
}
