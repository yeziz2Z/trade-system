package com.liuk.trade.common.rocketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.liuk.trade.common.constants.MQEnums;
import com.liuk.trade.common.exception.LiukMQException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kl on 2018/2/17.
 */
public class LiukMQProducer {

    public static final Logger LOGGER = LoggerFactory.getLogger(LiukMQProducer.class);

    private DefaultMQProducer producer;

    private String groupName;
    private String namesrvAddr;
    private int maxMessageSize = 1024 * 1024 * 4;//4M
    private int sendMsgTimeout = 10000;//10s

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public void setMaxMessageSize(int maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    public void setSendMsgTimeout(int sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    public void init() throws LiukMQException {
        if(StringUtils.isBlank(this.groupName)){
            throw new LiukMQException("groupName is blank!");
        }
        if(StringUtils.isBlank(this.namesrvAddr)){
            throw new LiukMQException("namesrvAddr is blank!");
        }
        this.producer  = new DefaultMQProducer(this.groupName);
        this.producer.setNamesrvAddr(this.namesrvAddr);
        this.producer.setMaxMessageSize(this.maxMessageSize);
        this.producer.setSendMsgTimeout(sendMsgTimeout);
//        this.producer.setVipChannelEnabled(false);
        try {
            this.producer.start();
            LOGGER.info(String.format("producer is start!groupName:[%s],namesrvAddr:[%s]",this.groupName,this.namesrvAddr));
        } catch (MQClientException e) {
            e.printStackTrace();
            LOGGER.error(String.format("producer is error!groupName:[%s],namesrvAddr:[%s]",this.groupName,this.namesrvAddr,e));
            throw new LiukMQException(e);
        }
    }

    public SendResult sendMessage(String topic,String tags,String keys,String messageTest) throws LiukMQException {
        if(StringUtils.isBlank(topic)){
            throw new LiukMQException("topic is blank!");
        }
        if(StringUtils.isBlank(messageTest)){
            throw new LiukMQException("messageTest is blank!");
        }
        Message message = new Message(topic,tags,keys,messageTest.getBytes());

        try {
            return this.producer.send(message);
        } catch (Exception e) {
            LOGGER.error("send message error:{}",e.getMessage(),e);
            throw new LiukMQException(e);
        }
    }
    public SendResult sendMessage(MQEnums.TopicEnum topicEnum, String keys, String messageTest) throws LiukMQException {
        return this.sendMessage(topicEnum.getTopic(),topicEnum.getTag(),keys,messageTest);
    }
}
