package com.liuk.trade.user.api;

import com.liuk.trade.common.api.IUserApi;
import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyReq;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import com.liuk.trade.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by kl on 2018/2/18.
 */
@Controller
public class UserApiImpl implements IUserApi {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/queryUserById",method = RequestMethod.POST)
    @ResponseBody
    public QueryUserRes queryUserById(@RequestBody  QueryUserReq queryUserReq) {
        return userService.queryUserById(queryUserReq);
    }

    @RequestMapping(value = "/changeUserMoney",method = RequestMethod.POST)
    @ResponseBody
    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq) {
        ChangeUserMoneyRes changeUserMoneyRes = new ChangeUserMoneyRes();
        try {
             changeUserMoneyRes = userService.changeUserMoney(changeUserMoneyReq);
        } catch (Exception e) {
            changeUserMoneyRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            changeUserMoneyRes.setRetInfo(e.getMessage());
        }
        return changeUserMoneyRes;
    }
}
