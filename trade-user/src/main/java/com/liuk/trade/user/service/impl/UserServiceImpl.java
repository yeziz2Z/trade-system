package com.liuk.trade.user.service.impl;

import com.liuk.trade.common.constants.TradeEnums;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyReq;
import com.liuk.trade.common.protocol.user.ChangeUserMoneyRes;
import com.liuk.trade.common.protocol.user.QueryUserReq;
import com.liuk.trade.common.protocol.user.QueryUserRes;
import com.liuk.trade.entity.TradeUser;
import com.liuk.trade.entity.TradeUserMoneyLog;
import com.liuk.trade.entity.TradeUserMoneyLogExample;
import com.liuk.trade.mapper.TradeUserMapper;
import com.liuk.trade.mapper.TradeUserMoneyLogMapper;
import com.liuk.trade.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kl on 2018/2/18.
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private TradeUserMapper tradeUserMapper;
    @Autowired
    private TradeUserMoneyLogMapper tradeUserMoneyLogMapper;


    public QueryUserRes queryUserById(QueryUserReq queryUserReq) {
        QueryUserRes queryUserRes = new QueryUserRes();
        queryUserRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        queryUserRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());
        try {
            if (queryUserReq == null || queryUserReq.getUserId() == null){
                throw new RuntimeException("请求参数有误！");
            }
            TradeUser user = tradeUserMapper.selectByPrimaryKey(queryUserReq.getUserId());
            if(user != null){
                BeanUtils.copyProperties(user,queryUserRes);
            }else {
                throw new RuntimeException("未查询到该用户！");
            }
        }catch (Exception ex){
            queryUserRes.setRetCode(TradeEnums.ResEnum.FAIL.getCode());
            queryUserRes.setRetInfo(TradeEnums.ResEnum.FAIL.getDesc());
        }
        return queryUserRes;
    }

    @Transactional
    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq) {
        ChangeUserMoneyRes changeUserMoneyRes = new ChangeUserMoneyRes();
        changeUserMoneyRes.setRetCode(TradeEnums.ResEnum.SUCCESS.getCode());
        changeUserMoneyRes.setRetInfo(TradeEnums.ResEnum.SUCCESS.getDesc());
        if (changeUserMoneyReq == null || changeUserMoneyReq.getUserId() == null || changeUserMoneyReq.getUserMoney() == null){
            throw new RuntimeException("请求参数不正确");
        }
        if(changeUserMoneyReq.getUserMoney().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("金额不能小于0");
        }
        TradeUserMoneyLog tradeUserMoneyLog = new TradeUserMoneyLog();
        tradeUserMoneyLog.setUserId(changeUserMoneyReq.getUserId());
        tradeUserMoneyLog.setOrderId(changeUserMoneyReq.getOrderId());
        tradeUserMoneyLog.setUserMoney(changeUserMoneyReq.getUserMoney());
        tradeUserMoneyLog.setMoneyLogType(changeUserMoneyReq.getMoneyLogType());
        tradeUserMoneyLog.setCreateTime(new Date());

        TradeUser tradeUser = new TradeUser();
        tradeUser.setUserId(changeUserMoneyReq.getUserId());
        tradeUser.setUserMoney(changeUserMoneyReq.getUserMoney());
        //查询付款记录
        TradeUserMoneyLogExample logExample = new TradeUserMoneyLogExample();
        logExample.createCriteria()
                .andUserIdEqualTo(changeUserMoneyReq.getUserId())
                .andOrderIdEqualTo(changeUserMoneyReq.getOrderId())
                .andMoneyLogTypeEqualTo(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode());
        long l = tradeUserMoneyLogMapper.countByExample(logExample);
        //订单付款
        if (changeUserMoneyReq.getMoneyLogType().equals(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode())){
            if (l>0){
                throw new RuntimeException("已经付款，不能再付款");
            }
            tradeUserMapper.reduceUserMoney(tradeUser);
        }
        //订单退款
        if (changeUserMoneyReq.getMoneyLogType().equals(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode())){

            if (l == 0){
                throw new RuntimeException("没有付款信息，不能退款");
            }
            logExample =  new TradeUserMoneyLogExample();
            logExample.createCriteria()
                    .andUserIdEqualTo(changeUserMoneyReq.getUserId())
                    .andOrderIdEqualTo(changeUserMoneyReq.getOrderId())
                    .andMoneyLogTypeEqualTo(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode());
            l = tradeUserMoneyLogMapper.countByExample(logExample);
            if (l > 1){
                throw new RuntimeException("已经退款过了，不能退款");
            }
            tradeUserMapper.addUserMoney(tradeUser);
        }
        tradeUserMoneyLogMapper.insert(tradeUserMoneyLog);

        return changeUserMoneyRes;
    }
}
