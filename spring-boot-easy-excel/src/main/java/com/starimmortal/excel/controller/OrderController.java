package com.starimmortal.excel.controller;

import com.starimmortal.core.constant.StringConstant;
import com.starimmortal.core.util.DateUtil;
import com.starimmortal.excel.dto.OrderExcelDTO;
import com.starimmortal.excel.pojo.OrderDO;
import com.starimmortal.excel.pojo.ProductDO;
import com.starimmortal.excel.pojo.UserDO;
import com.starimmortal.excel.util.ExcelUtil;
import com.starimmortal.excel.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单前端控制器
 *
 * @author william@StarImmortal
 * @date 2023/03/11
 */
@RestController
@RequestMapping("/order")
@Validated
@Api(tags = "订单模块")
public class OrderController {

	@ApiOperation(value = "导出订单")
	@GetMapping("/export")
	public void exportOrder(HttpServletResponse response) {
		List<OrderDO> orderList = getOrderList();
		// 平铺订单数据
		List<OrderExcelDTO> exportData = convertOrderBean(orderList);
		String fileName = "订单列表" + StringConstant.SNAKE_SEPARATOR
				+ DateUtil.getCurrentDay(DateUtil.TIME_WITH_UNDERLINE);
		ExcelUtil.exportExcel(response, fileName, "订单列表", OrderExcelDTO.class, exportData, true, true);
	}

	/**
	 * 获取订单列表数据
	 * @return 订单列表数据
	 */
	private List<OrderDO> getOrderList() {
		List<OrderDO> orderList = JsonUtil.readJsonArrayFile("mock/orders.json", OrderDO.class);
		List<ProductDO> productList = JsonUtil.readJsonArrayFile("mock/products.json", ProductDO.class);
		List<UserDO> userList = JsonUtil.readJsonArrayFile("mock/users.json", UserDO.class);
		for (int i = 0; i < orderList.size(); i++) {
			OrderDO order = orderList.get(i);
			order.setUser(userList.get(i));
			order.setProductList(productList);
		}
		return orderList;
	}

	/**
	 * 进行订单数据转换与平铺（DO -> BO）
	 * @param orderList 原始订单数据
	 * @return 转换后订单数据
	 */
	private List<OrderExcelDTO> convertOrderBean(List<OrderDO> orderList) {
		// TODO 使用 MapStruct 优化
		List<OrderExcelDTO> result = new ArrayList<>();
		orderList.forEach(order -> {
			List<ProductDO> productList = order.getProductList();
			productList.forEach(product -> {
				OrderExcelDTO orderData = new OrderExcelDTO();
				BeanUtils.copyProperties(product, orderData);
				BeanUtils.copyProperties(order, orderData);
				result.add(orderData);
			});
		});
		return result;
	}

}
