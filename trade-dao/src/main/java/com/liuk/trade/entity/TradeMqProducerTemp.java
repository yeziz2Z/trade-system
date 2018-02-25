package com.liuk.trade.entity;

import java.io.Serializable;
import java.util.Date;

public class TradeMqProducerTemp implements Serializable {
    private String id;

    private String groupName;

    private String msgTopic;

    private String msgTag;

    private String msgKeys;

    private String msgBody;

    private Date createTime;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getMsgTopic() {
        return msgTopic;
    }

    public void setMsgTopic(String msgTopic) {
        this.msgTopic = msgTopic == null ? null : msgTopic.trim();
    }

    public String getMsgTag() {
        return msgTag;
    }

    public void setMsgTag(String msgTag) {
        this.msgTag = msgTag == null ? null : msgTag.trim();
    }

    public String getMsgKeys() {
        return msgKeys;
    }

    public void setMsgKeys(String msgKeys) {
        this.msgKeys = msgKeys == null ? null : msgKeys.trim();
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody == null ? null : msgBody.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupName=").append(groupName);
        sb.append(", msgTopic=").append(msgTopic);
        sb.append(", msgTag=").append(msgTag);
        sb.append(", msgKeys=").append(msgKeys);
        sb.append(", msgBody=").append(msgBody);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TradeMqProducerTemp other = (TradeMqProducerTemp) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupName() == null ? other.getGroupName() == null : this.getGroupName().equals(other.getGroupName()))
            && (this.getMsgTopic() == null ? other.getMsgTopic() == null : this.getMsgTopic().equals(other.getMsgTopic()))
            && (this.getMsgTag() == null ? other.getMsgTag() == null : this.getMsgTag().equals(other.getMsgTag()))
            && (this.getMsgKeys() == null ? other.getMsgKeys() == null : this.getMsgKeys().equals(other.getMsgKeys()))
            && (this.getMsgBody() == null ? other.getMsgBody() == null : this.getMsgBody().equals(other.getMsgBody()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupName() == null) ? 0 : getGroupName().hashCode());
        result = prime * result + ((getMsgTopic() == null) ? 0 : getMsgTopic().hashCode());
        result = prime * result + ((getMsgTag() == null) ? 0 : getMsgTag().hashCode());
        result = prime * result + ((getMsgKeys() == null) ? 0 : getMsgKeys().hashCode());
        result = prime * result + ((getMsgBody() == null) ? 0 : getMsgBody().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }
}