package com.gxzx.testrabbitmq.web.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 行情配置表
 * </p>
 *
 * @author administrator
 * @since 2019-08-21
 */
public class Market extends Model<Market> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 交易币种ID
     */
    private Integer coinId;

    /**
     * 币种
     */
    private String name;

    /**
     * 计价币种ID
     */
    private Integer pointCoinId;

    private String round;

    /**
     * 买入手续费
     */
    private String feeBuy;

    /**
     * 卖出手续费
     */
    private String feeSell;

    /**
     * 买入最小交易价
     */
    private String buyMin;

    /**
     * 买入最小交易价
     */
    private String buyMax;

    /**
     * 卖出最小交易价
     */
    private String sellMin;

    /**
     *  卖出最大交易价
     */
    private String sellMax;

    /**
     * 单笔最小交易额
     */
    private String tradeMin;

    /**
     * 单笔最大交易额
     */
    private String tradeMax;

    private String invitBuy;

    private String invitSell;

    /**
     * 涨幅限制（单位：%）
     */
    private String zhang;

    /**
     * 跌幅限制（单位：%）
     */
    private String die;

    /**
     * 前一天最后一笔交易金额
     */
    private String houPrice;

    /**
     * 价格小数位限制
     */
    private Integer floatPrice;

    /**
     * 数量小数位限制
     */
    private Integer floatNumber;

    private String tendency;

    /**
     * 走势
     */
    private String zoushi;

    /**
     * 是否开启交易，0：禁止交易，1：开启交易
     */
    private Integer trade;

    private BigDecimal newPrice;

    /**
     * 买价（最佳卖价）
     */
    private BigDecimal buyPrice;

    /**
     * 卖价（最佳买价）
     */
    private BigDecimal sellPrice;

    /**
     * 最低价
     */
    private BigDecimal minPrice;

    /**
     * 最高价
     */
    private BigDecimal maxPrice;

    /**
     * 成交量
     */
    private BigDecimal volume;

    /**
     * 日涨跌
     */
    private BigDecimal dayChange;

    private BigDecimal apiMin;

    private BigDecimal apiMax;

    private Integer sort;

    private Date createTime;

    private Date updateTime;

    /**
     * 开启时间
     */
    private String openTime;

    /**
     * 关闭时间
     */
    private String closeTime;

    /**
     * 状态，0：禁用，1：可用（币种是否显示）
     */
    private Integer status;

    /**
     * 热门状态 0：非热门，1:热门
     */
    private Boolean hot;

    /**
     * 是否删除0、不删除1、已删除
     */
    private Integer isDel;

    private Integer maxbuy;

    private Integer minsell;

    private Integer region;

    /**
     * 撮合交换机名称(一个撮合支持一或多个币对市场)，这个字段不能提供给运营配置
     */
    private String matcherExchangeName;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCoinId() {
        return coinId;
    }

    public void setCoinId(Integer coinId) {
        this.coinId = coinId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPointCoinId() {
        return pointCoinId;
    }

    public void setPointCoinId(Integer pointCoinId) {
        this.pointCoinId = pointCoinId;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getFeeBuy() {
        return feeBuy;
    }

    public void setFeeBuy(String feeBuy) {
        this.feeBuy = feeBuy;
    }

    public String getFeeSell() {
        return feeSell;
    }

    public void setFeeSell(String feeSell) {
        this.feeSell = feeSell;
    }

    public String getBuyMin() {
        return buyMin;
    }

    public void setBuyMin(String buyMin) {
        this.buyMin = buyMin;
    }

    public String getBuyMax() {
        return buyMax;
    }

    public void setBuyMax(String buyMax) {
        this.buyMax = buyMax;
    }

    public String getSellMin() {
        return sellMin;
    }

    public void setSellMin(String sellMin) {
        this.sellMin = sellMin;
    }

    public String getSellMax() {
        return sellMax;
    }

    public void setSellMax(String sellMax) {
        this.sellMax = sellMax;
    }

    public String getTradeMin() {
        return tradeMin;
    }

    public void setTradeMin(String tradeMin) {
        this.tradeMin = tradeMin;
    }

    public String getTradeMax() {
        return tradeMax;
    }

    public void setTradeMax(String tradeMax) {
        this.tradeMax = tradeMax;
    }

    public String getInvitBuy() {
        return invitBuy;
    }

    public void setInvitBuy(String invitBuy) {
        this.invitBuy = invitBuy;
    }

    public String getInvitSell() {
        return invitSell;
    }

    public void setInvitSell(String invitSell) {
        this.invitSell = invitSell;
    }

    public String getZhang() {
        return zhang;
    }

    public void setZhang(String zhang) {
        this.zhang = zhang;
    }

    public String getDie() {
        return die;
    }

    public void setDie(String die) {
        this.die = die;
    }

    public String getHouPrice() {
        return houPrice;
    }

    public void setHouPrice(String houPrice) {
        this.houPrice = houPrice;
    }

    public Integer getFloatPrice() {
        return floatPrice;
    }

    public void setFloatPrice(Integer floatPrice) {
        this.floatPrice = floatPrice;
    }

    public Integer getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(Integer floatNumber) {
        this.floatNumber = floatNumber;
    }

    public String getTendency() {
        return tendency;
    }

    public void setTendency(String tendency) {
        this.tendency = tendency;
    }

    public String getZoushi() {
        return zoushi;
    }

    public void setZoushi(String zoushi) {
        this.zoushi = zoushi;
    }

    public Integer getTrade() {
        return trade;
    }

    public void setTrade(Integer trade) {
        this.trade = trade;
    }

    public BigDecimal getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(BigDecimal newPrice) {
        this.newPrice = newPrice;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getDayChange() {
        return dayChange;
    }

    public void setDayChange(BigDecimal dayChange) {
        this.dayChange = dayChange;
    }

    public BigDecimal getApiMin() {
        return apiMin;
    }

    public void setApiMin(BigDecimal apiMin) {
        this.apiMin = apiMin;
    }

    public BigDecimal getApiMax() {
        return apiMax;
    }

    public void setApiMax(BigDecimal apiMax) {
        this.apiMax = apiMax;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getHot() {
        return hot;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getMaxbuy() {
        return maxbuy;
    }

    public void setMaxbuy(Integer maxbuy) {
        this.maxbuy = maxbuy;
    }

    public Integer getMinsell() {
        return minsell;
    }

    public void setMinsell(Integer minsell) {
        this.minsell = minsell;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public String getMatcherExchangeName() {
        return matcherExchangeName;
    }

    public void setMatcherExchangeName(String matcherExchangeName) {
        this.matcherExchangeName = matcherExchangeName;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Market{" +
        "id=" + id +
        ", coinId=" + coinId +
        ", name=" + name +
        ", pointCoinId=" + pointCoinId +
        ", round=" + round +
        ", feeBuy=" + feeBuy +
        ", feeSell=" + feeSell +
        ", buyMin=" + buyMin +
        ", buyMax=" + buyMax +
        ", sellMin=" + sellMin +
        ", sellMax=" + sellMax +
        ", tradeMin=" + tradeMin +
        ", tradeMax=" + tradeMax +
        ", invitBuy=" + invitBuy +
        ", invitSell=" + invitSell +
        ", zhang=" + zhang +
        ", die=" + die +
        ", houPrice=" + houPrice +
        ", floatPrice=" + floatPrice +
        ", floatNumber=" + floatNumber +
        ", tendency=" + tendency +
        ", zoushi=" + zoushi +
        ", trade=" + trade +
        ", newPrice=" + newPrice +
        ", buyPrice=" + buyPrice +
        ", sellPrice=" + sellPrice +
        ", minPrice=" + minPrice +
        ", maxPrice=" + maxPrice +
        ", volume=" + volume +
        ", dayChange=" + dayChange +
        ", apiMin=" + apiMin +
        ", apiMax=" + apiMax +
        ", sort=" + sort +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", openTime=" + openTime +
        ", closeTime=" + closeTime +
        ", status=" + status +
        ", hot=" + hot +
        ", isDel=" + isDel +
        ", maxbuy=" + maxbuy +
        ", minsell=" + minsell +
        ", region=" + region +
        ", matcherExchangeName=" + matcherExchangeName +
        "}";
    }
}
