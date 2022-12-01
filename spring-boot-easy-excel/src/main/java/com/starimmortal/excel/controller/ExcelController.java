package com.starimmortal.excel.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.excel.bo.OrderBO;
import com.starimmortal.excel.pojo.OrderDO;
import com.starimmortal.excel.pojo.ProductDO;
import com.starimmortal.excel.pojo.UserDO;
import com.starimmortal.excel.strategy.ExcelCellMergeStrategy;
import com.starimmortal.excel.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel导入导出
 *
 * @author william@StarImmortal
 */
@RestController
@RequestMapping("/excel")
@Api(tags = "EasyExcel")
public class ExcelController {

    @SneakyThrows(IOException.class)
    @ApiOperation(value = "导出用户列表")
    @GetMapping("/export/user")
    public void exportUserExcel(HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, "用户列表");
            List<UserDO> userList = JsonUtil.readJsonArrayFile("mock/users.json", UserDO.class);
            EasyExcel.write(response.getOutputStream())
                    .head(UserDO.class)
                    .registerWriteHandler(this.customStyleStrategy())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("用户列表")
                    .doWrite(userList);
        } catch (IOException e) {
            this.setErrorResponse(response);
        }
    }

    @ApiOperation(value = "导入用户列表")
    @PostMapping("/import/user")
    public ResponseVO importUserExcel(@RequestPart(value = "file") MultipartFile file) {
        try {
            List<UserDO> userList = EasyExcel.read(file.getInputStream())
                    .head(UserDO.class)
                    .sheet()
                    .doReadSync();
            return ResponseVO.success(userList);
        } catch (IOException e) {
            return ResponseVO.error();
        }
    }

    @SneakyThrows(IOException.class)
    @ApiOperation(value = "导出订单列表")
    @GetMapping("/export/order")
    public void exportOrderExcel(HttpServletResponse response) {
        try {
            this.setExcelResponseProp(response, "订单列表");
            List<OrderDO> orderList = this.getOrderList();
            // 平铺订单数据
            List<OrderBO> exportData = this.convert(orderList);
            EasyExcel.write(response.getOutputStream())
                    .head(OrderBO.class)
                    .registerWriteHandler(new ExcelCellMergeStrategy(OrderBO.class))
                    .registerWriteHandler(this.customStyleStrategy())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet("订单列表")
                    .doWrite(exportData);
        } catch (IOException e) {
            this.setErrorResponse(response);
        }
    }

    /**
     * 设置响应结果
     *
     * @param response    响应结果对象
     * @param rawFileName 文件名
     * @throws UnsupportedEncodingException 不支持编码异常
     */
    private void setExcelResponseProp(HttpServletResponse response, String rawFileName) throws UnsupportedEncodingException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode(rawFileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
    }

    /**
     * 设置错误响应结果
     *
     * @param response 响应结果对象
     */
    private void setErrorResponse(HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        Map<String, Object> map = new HashMap<>(16);
        map.put("code", -1);
        map.put("message", "下载文件失败");
        response.getWriter().println(JsonUtil.objectMapper.writeValueAsString(map));
    }

    /**
     * 自定义样式
     *
     * @return 样式策略
     */
    private HorizontalCellStyleStrategy customStyleStrategy() {
        // 表头策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 设置内容水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置内容垂直居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 获取订单列表数据
     *
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
     *
     * @param orderList 原始订单数据
     * @return 转换后订单数据
     */
    private List<OrderBO> convert(List<OrderDO> orderList) {
        List<OrderBO> result = new ArrayList<>();
        orderList.forEach(order -> {
            List<ProductDO> productList = order.getProductList();
            productList.forEach(product -> {
                OrderBO orderData = new OrderBO();
                BeanUtils.copyProperties(product, orderData);
                BeanUtils.copyProperties(order, orderData);
                result.add(orderData);
            });
        });
        return result;
    }
}
