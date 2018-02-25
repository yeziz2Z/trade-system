package com.liuk.trade.common.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.liuk.trade.common.exception.LiukMQException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kl on 2018/2/17.
 */
public class LiukMQConsumer {
    public static final Logger LOGGER = LoggerFactory.getLogger(LiukMQConsumer.class);

    private IMessageProcessor processor;

    private DefaultMQPushConsumer consumer;
    private String groupName;
    private String topic;
    private String tag = "*";//多个 tag 以 || 分割
    private String namesrvAddr;
    private int consumeThreadMin = 20;//最小线程数
    private int consumeThreadMax = 64;//最大线程数

    public void setProcessor(IMessageProcessor processor) {
        this.processor = processor;
    }

    public void setConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public void init() throws LiukMQException {
        System.out.println(groupName);
        if(StringUtils.isBlank(this.groupName)){
            throw new LiukMQException("groupName is blank!");
        }
        if(StringUtils.isBlank(this.namesrvAddr)){
            throw new LiukMQException("namesrvAddr is blank!");
        }
        if(StringUtils.isBlank(this.topic)){
            throw new LiukMQException("topic is blank!");
        }

        DefaultMQPushConsumer  consumer = new DefaultMQPushConsumer(this.groupName);
        consumer.setNamesrvAddr(this.namesrvAddr);
        try {
            consumer.subscribe(this.topic, this.tag);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setConsumeThreadMin(this.consumeThreadMin);
            consumer.setConsumeThreadMax(this.consumeThreadMax);

            LiukMessageListener liukMessageListener = new LiukMessageListener();
            liukMessageListener.setMessageProcessor(processor);
            consumer.registerMessageListener(liukMessageListener);

            consumer.start();
            LOGGER.info(String.format("consumer is start!groupName:{},topic:{},namesrvAddr:{}",groupName,topic,namesrvAddr));
        } catch (MQClientException e) {
            LOGGER.error(String.format("consumer is error!groupName:{},topic:{},namesrvAddr:{}",groupName,topic,namesrvAddr,e));
            throw new LiukMQException(e);
        }
    }
}
