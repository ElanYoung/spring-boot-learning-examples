package com.starimmortal.excel.pojo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品
 *
 * @author william@StarImmortal
 */
@Data
public class ProductDO {
    /**
     * 主键
     */
    private String id;

    /**
     * 商品编号
     */
    private String productId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品标题
     */
    private String subtitle;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private Integer count;
}
