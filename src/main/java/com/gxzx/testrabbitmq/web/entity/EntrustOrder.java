package com.gxzx.testrabbitmq.web.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 委托单
 * </p>
 *
 * @author administrator
 * @since 2019-08-21
 */
public class EntrustOrder extends Model<EntrustOrder> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 市场（币对）id
     */
    private Integer marketId;

    /**
     * 市场（币对）编码，例如BTC_USDT
     */
    private String marketCode;

    /**
     * 委托（挂单）总数量
     */
    private BigDecimal entrustVolume;

    /**
     * 限价单委托（挂单）单价
     */
    private BigDecimal entrustPrice;

    /**
     * 已成交数量
     */
    private BigDecimal dealVolume;

    /**
     * 已成交平均单价
     */
    private BigDecimal dealAvgPrice;

    /**
     * 手续费费率
     */
    private BigDecimal feeRate;

    /**
     * 手续费费率折扣
     */
    private BigDecimal feeRateDiscount;

    /**
     * 手续费冻结总金额
     */
    private BigDecimal frozenFee;

    /**
     * 已收取手续费
     */
    private BigDecimal totalDealFee;

    private Long userId;

    /**
     * 订单类型（1币币 2杠杆）
     */
    private Integer orderType;

    /**
     * 交易类型1买2卖
     */
    private Integer tradeType;

    /**
     * 委托类型（1限价 2市价）
     */
    private Integer entrustWay;

    /**
     * 状态（1 未成交 2 部分成交 3全部成交 4 撤销）
     */
    private Integer status;

    /**
     * 委托（挂单）时间
     */
    private Date entrustTime;

    /**
     * 订单创建（入库）时间（格林威治标准时间）
     */
    private Date gmtCreatedTime;

    /**
     * 订单更新（入库）时间（格林威治标准时间）
     */
    private Date gmtModifiedTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMarketId() {
        return marketId;
    }

    public void setMarketId(Integer marketId) {
        this.marketId = marketId;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public BigDecimal getEntrustVolume() {
        return entrustVolume;
    }

    public void setEntrustVolume(BigDecimal entrustVolume) {
        this.entrustVolume = entrustVolume;
    }

    public BigDecimal getEntrustPrice() {
        return entrustPrice;
    }

    public void setEntrustPrice(BigDecimal entrustPrice) {
        this.entrustPrice = entrustPrice;
    }

    public BigDecimal getDealVolume() {
        return dealVolume;
    }

    public void setDealVolume(BigDecimal dealVolume) {
        this.dealVolume = dealVolume;
    }

    public BigDecimal getDealAvgPrice() {
        return dealAvgPrice;
    }

    public void setDealAvgPrice(BigDecimal dealAvgPrice) {
        this.dealAvgPrice = dealAvgPrice;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public BigDecimal getFeeRateDiscount() {
        return feeRateDiscount;
    }

    public void setFeeRateDiscount(BigDecimal feeRateDiscount) {
        this.feeRateDiscount = feeRateDiscount;
    }

    public BigDecimal getFrozenFee() {
        return frozenFee;
    }

    public void setFrozenFee(BigDecimal frozenFee) {
        this.frozenFee = frozenFee;
    }

    public BigDecimal getTotalDealFee() {
        return totalDealFee;
    }

    public void setTotalDealFee(BigDecimal totalDealFee) {
        this.totalDealFee = totalDealFee;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer tradeType) {
        this.tradeType = tradeType;
    }

    public Integer getEntrustWay() {
        return entrustWay;
    }

    public void setEntrustWay(Integer entrustWay) {
        this.entrustWay = entrustWay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getEntrustTime() {
        return entrustTime;
    }

    public void setEntrustTime(Date entrustTime) {
        this.entrustTime = entrustTime;
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
        return this.id;
    }

    @Override
    public String toString() {
        return "EntrustOrder{" +
        "id=" + id +
        ", marketId=" + marketId +
        ", marketCode=" + marketCode +
        ", entrustVolume=" + entrustVolume +
        ", entrustPrice=" + entrustPrice +
        ", dealVolume=" + dealVolume +
        ", dealAvgPrice=" + dealAvgPrice +
        ", feeRate=" + feeRate +
        ", feeRateDiscount=" + feeRateDiscount +
        ", frozenFee=" + frozenFee +
        ", totalDealFee=" + totalDealFee +
        ", userId=" + userId +
        ", orderType=" + orderType +
        ", tradeType=" + tradeType +
        ", entrustWay=" + entrustWay +
        ", status=" + status +
        ", entrustTime=" + entrustTime +
        ", gmtCreatedTime=" + gmtCreatedTime +
        ", gmtModifiedTime=" + gmtModifiedTime +
        "}";
    }
}
