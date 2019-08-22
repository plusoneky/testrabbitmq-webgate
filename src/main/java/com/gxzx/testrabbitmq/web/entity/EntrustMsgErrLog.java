package com.gxzx.testrabbitmq.web.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author administrator
 * @since 2019-08-21
 */
public class EntrustMsgErrLog extends Model<EntrustMsgErrLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 委托单ID
     */
    private Long entrustId;

    private Long userId;

    /**
     * 可用余额账户ID
     */
    private Long availableBalanceAccountId;

    /**
     * 可用账户ID
     */
    private Long freezingBalanceAccountId;

    private BigDecimal amount;

    /**
     * 处理结果（0 不需要回滚预估值  1需要回滚预估值）
     */
    private Integer result;

    /**
     * 处理状态 0 未处理  1 已处理 
     */
    private Integer status;

    private Date gmtCreatedTime;

    private Date gmtModifiedTime;


    public Long getEntrustId() {
        return entrustId;
    }

    public void setEntrustId(Long entrustId) {
        this.entrustId = entrustId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAvailableBalanceAccountId() {
        return availableBalanceAccountId;
    }

    public void setAvailableBalanceAccountId(Long availableBalanceAccountId) {
        this.availableBalanceAccountId = availableBalanceAccountId;
    }

    public Long getFreezingBalanceAccountId() {
        return freezingBalanceAccountId;
    }

    public void setFreezingBalanceAccountId(Long freezingBalanceAccountId) {
        this.freezingBalanceAccountId = freezingBalanceAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getGmtCreatedTime() {
        return gmtCreatedTime;
    }

    public void setGmtCreatedTime(Date gmtCreatedTime) {
        this.gmtCreatedTime = gmtCreatedTime;
    }

    public Date getGmtModifiedTime() {
        return gmtModifiedTime;
    }

    public void setGmtModifiedTime(Date gmtModifiedTime) {
        this.gmtModifiedTime = gmtModifiedTime;
    }

    @Override
    protected Serializable pkVal() {
        return this.entrustId;
    }

    @Override
    public String toString() {
        return "EntrustMsgErrLog{" +
        "entrustId=" + entrustId +
        ", userId=" + userId +
        ", availableBalanceAccountId=" + availableBalanceAccountId +
        ", freezingBalanceAccountId=" + freezingBalanceAccountId +
        ", amount=" + amount +
        ", result=" + result +
        ", status=" + status +
        ", gmtCreatedTime=" + gmtCreatedTime +
        ", gmtModifiedTime=" + gmtModifiedTime +
        "}";
    }
}
