package me.zeanzai.shardingmultids.domain.query;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shawnwang
 */
@Data
public class OrderInfoBaseQuery extends BaseQuery implements Serializable {
    /**
     *订单号
     */
    private String orderNo;
    /**
     * 订单状态,10待付款，20待接单，30已接单，40配送中，50已完成，55部分退款，60全部退款，70取消订单'
     */
    private Integer orderStatus;

    /**
     *
     * 支付状态,1待支付,2支付成功
     */
    private Integer payStatus;
    /**
     *
     * 用户ID
     */
    private Long userId;
    /**
     *
     * 商户ID
     */
    private Long merchantId;
    /**
     *
     *'交易时间开始时间'
     */
    private Date startTransTime;
    /**
     *
     *'交易时间结束时间'
     */
    private Date endTransTime;
    /**
     * 当前页
     */
    protected Integer current;
    /**
     * 条数
     */
    protected Integer size;
}

