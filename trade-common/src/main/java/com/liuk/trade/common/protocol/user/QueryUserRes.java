package com.liuk.trade.common.protocol.user;

import com.liuk.trade.common.protocol.BaseRes;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by kl on 2018/2/18.
 */
public class QueryUserRes extends BaseRes {
    private Integer userId;

    private String userName;

    private String userMobile;

    private Integer userScore;

    private Date userRefTime;

    private BigDecimal userMoney;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public Date getUserRefTime() {
        return userRefTime;
    }

    public void setUserRefTime(Date userRefTime) {
        this.userRefTime = userRefTime;
    }

    public BigDecimal getUserMoney() {
        return userMoney;
    }

    public void setUserMoney(BigDecimal userMoney) {
        this.userMoney = userMoney;
    }

    @Override
    public String toString() {
        return "QueryUserRes{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userMobile='" + userMobile + '\'' +
                ", userScore=" + userScore +
                ", userRefTime=" + userRefTime +
                ", userMoney=" + userMoney +
                '}';
    }
}
