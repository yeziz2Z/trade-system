package com.liuk.trade.common.api;

import com.liuk.trade.common.protocol.user.ChangeUserMoneyReq;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;

/**
 * Created by kl on 2018/2/18.
 */
public interface IUserApi {

    QueryUserRes queryUserById(QueryUserReq queryUserReq);

    ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq);
}
