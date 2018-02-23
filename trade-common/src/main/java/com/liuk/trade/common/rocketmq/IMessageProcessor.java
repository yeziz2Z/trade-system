package com.liuk.trade.common.rocketmq;

import com.alibaba.rocketmq.common.message.MessageExt;

/**
 * Created by kl on 2018/2/17.
 */
public interface IMessageProcessor {
    /**
     * 处理消息
     * @param messageExt
     * @return
     */
    boolean handleMessage(MessageExt messageExt);
}
