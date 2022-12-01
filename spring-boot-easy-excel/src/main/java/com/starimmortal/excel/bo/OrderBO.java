package com.starimmortal.excel.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.starimmortal.excel.annotation.ExcelMerge;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author william@StarImmortal
 */
@Data
public class OrderBO {
    @ExcelProperty(value = "订单主键")
    @ColumnWidth(16)
    @ExcelMerge(merge = true, isPrimaryKey = true)
    private String id;

    @ExcelProperty(value = "订单编号")
    @ColumnWidth(20)
    @ExcelMerge(merge = true)
    private String orderId;

    @ExcelProperty(value = "收货地址")
    @ExcelMerge(merge = true)
    @ColumnWidth(20)
    private String address;

    @ExcelProperty(value = "创建时间")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelMerge(merge = true)
    private Date createTime;

    @ExcelProperty(value = {"商品信息", "商品编号"})
    @ColumnWidth(20)
    private String productId;

    @ExcelProperty(value = {"商品信息", "商品名称"})
    @ColumnWidth(20)
    private String name;

    @ExcelProperty(value = {"商品信息", "商品标题"})
    @ColumnWidth(30)
    private String subtitle;

    @ExcelProperty(value = {"商品信息", "品牌名称"})
    @ColumnWidth(20)
    private String brandName;

    @ExcelProperty(value = {"商品信息", "商品价格"})
    @ColumnWidth(20)
    private BigDecimal price;

    @ExcelProperty(value = {"商品信息", "商品数量"})
    @ColumnWidth(20)
    private Integer count;
}
