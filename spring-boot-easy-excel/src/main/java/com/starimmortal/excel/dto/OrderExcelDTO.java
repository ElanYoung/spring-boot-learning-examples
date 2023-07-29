package com.starimmortal.excel.dto;

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
public class OrderExcelDTO {

	/**
	 * 订单主键
	 */
	@ExcelProperty(value = "订单主键")
	@ColumnWidth(16)
	@ExcelMerge(isPrimaryKey = true)
	private String id;

	/**
	 * 订单编号
	 */
	@ExcelProperty(value = "订单编号")
	@ColumnWidth(20)
	@ExcelMerge
	private String orderId;

	/**
	 * 收货地址
	 */
	@ExcelProperty(value = "收货地址")
	@ExcelMerge
	@ColumnWidth(20)
	private String address;

	/**
	 * 创建时间
	 */
	@ExcelProperty(value = "创建时间")
	@ColumnWidth(20)
	@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
	@ExcelMerge
	private Date createTime;

	/**
	 * 商品编号
	 */
	@ExcelProperty(value = { "商品信息", "商品编号" })
	@ColumnWidth(20)
	private String productId;

	/**
	 * 商品名称
	 */
	@ExcelProperty(value = { "商品信息", "商品名称" })
	@ColumnWidth(20)
	private String name;

	/**
	 * 商品标题
	 */
	@ExcelProperty(value = { "商品信息", "商品标题" })
	@ColumnWidth(30)
	private String subtitle;

	/**
	 * 品牌名称
	 */
	@ExcelProperty(value = { "商品信息", "品牌名称" })
	@ColumnWidth(20)
	private String brandName;

	/**
	 * 商品价格
	 */
	@ExcelProperty(value = { "商品信息", "商品价格" })
	@ColumnWidth(20)
	private BigDecimal price;

	/**
	 * 商品数量
	 */
	@ExcelProperty(value = { "商品信息", "商品数量" })
	@ColumnWidth(20)
	private Integer count;

}
