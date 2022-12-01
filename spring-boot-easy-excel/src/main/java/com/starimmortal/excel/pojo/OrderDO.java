package com.starimmortal.excel.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 订单
 *
 * @author william@StarImmortal
 */
@Data
public class OrderDO {
    /**
     * 主键
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 用户信息
     */
    private UserDO user;

    /**
     * 商品列表
     */
    private List<ProductDO> productList;

    /**
     * 订单创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
