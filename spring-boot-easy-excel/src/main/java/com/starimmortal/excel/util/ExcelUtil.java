package com.starimmortal.excel.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.excel.functional.PageQuery;
import com.starimmortal.excel.strategy.ExcelCellMergeStrategy;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * EasyExcel工具类
 *
 * @author william@StarImmortal
 * @date 2022/03/09
 */
@Slf4j
@NoArgsConstructor
public class ExcelUtil {
    /**
     * 单个Sheet允许最大行数
     */
    public static final int MAX_SHEET_ROW = 1048575;

    /**
     * 每一个Sheet最大存放数据量
     */
    public static final int MAX_ROW_COUNT_PER_SHEET = 200000;

    /**
     * 每一个Sheet最大查询/写入数据量
     */
    public static final int MAX_QUERY_WRITE_COUNT_PER_SHEET = 100000;

    /**
     * 导入Excel数据
     *
     * @param inputStream  文件流
     * @param clazz        数据类型
     * @param readListener 数据读取监听器
     */
    public static <T> void importExcel(InputStream inputStream, Class<?> clazz, ReadListener<T> readListener) {
        EasyExcel.read(inputStream, clazz, readListener)
                .ignoreEmptyRow(Boolean.TRUE)
                .autoCloseStream(Boolean.TRUE)
                .doReadAll();
    }

    /**
     * 不合并表头导出Excel文件（默认导出方式）
     *
     * @param response  响应结果
     * @param fileName  文件名
     * @param sheetName 单元名
     * @param clazz     数据类型
     * @param list      数据集合
     */
    @SneakyThrows
    public static <T> void exportExcel(HttpServletResponse response, String fileName, String sheetName, Class<?> clazz, List<T> list) {
        exportExcel(response, fileName, sheetName, clazz, list, false, true);
    }

    /**
     * 自定义导出Excel数据
     *
     * @param response        响应结果
     * @param fileName        文件名
     * @param sheetName       单元名
     * @param clazz           数据类型
     * @param list            数据集合
     * @param needMerge       是否合并单元格
     * @param needCustomStyle 是否自定义样式设置
     */
    @SneakyThrows
    public static <T> void exportExcel(HttpServletResponse response, String fileName, String sheetName, Class<?> clazz, List<T> list, Boolean needMerge, Boolean needCustomStyle) {
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).head(clazz).excelType(ExcelTypeEnum.XLSX).build();
            int maxSize = list.size() > MAX_SHEET_ROW ? MAX_SHEET_ROW : MAX_ROW_COUNT_PER_SHEET;
            int sheetNum = calculateSheetNum(list.size(), maxSize);
            for (int i = 0; i < sheetNum; i++) {
                ExcelWriterSheetBuilder writerSheetBuilder = buildExcelWriterSheet(i, sheetName + (i + 1), clazz, needMerge, needCustomStyle);
                List<T> writeList = list.stream().skip(sheetNum + 1).limit(maxSize).collect(Collectors.toList());
                excelWriter.write(writeList, writerSheetBuilder.build());
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
        } catch (Exception e) {
            log.error("导出{}文件失败：", fileName, e);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JsonUtil.objectMapper.writeValueAsString(ResponseVO.error()));
        } finally {
            Optional.ofNullable(excelWriter).ifPresent(ExcelWriter::finish);
        }
    }

    /**
     * 分页导出Excel数据（适用于大批量数据导出）
     *
     * @param response        响应结果
     * @param fileName        文件名
     * @param sheetName       单元名
     * @param clazz           数据类型
     * @param totalCount      数据总数
     * @param pageQuery       分页查询接口
     * @param needMerge       是否合并单元格
     * @param needCustomStyle 是否自定义样式设置
     */
    @SneakyThrows
    public static <T> void exportExcel(HttpServletResponse response, String fileName, String sheetName, Class<?> clazz, Long totalCount, PageQuery<T> pageQuery, Boolean needMerge, Boolean needCustomStyle) {
        // TODO 此导出目前存在一个问题：当 MAX_ROW_COUNT_PER_SHEET % MAX_QUERY_WRITE_COUNT_PER_SHEET != 0 时，会导致一个Sheet写入的数据行数超过 MAX_ROW_COUNT_PER_SHEET
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(response.getOutputStream()).head(clazz).excelType(ExcelTypeEnum.XLSX).build();
            // 计算 Sheet 数量
            long sheetNum = calculateSheetNum(Math.toIntExact(totalCount), MAX_ROW_COUNT_PER_SHEET);
            // 计算一般情况下每一个 Sheet 需要写入的次数（一般情况不包含最后一个 Sheet，因为最后一个 Sheet 不确定会写入多少条数据）
            long sheetWriteCount = MAX_ROW_COUNT_PER_SHEET / MAX_QUERY_WRITE_COUNT_PER_SHEET;
            // 需要确定最后一个 Sheet 写入的数据行数是多少
            long lastSheetRowCount = totalCount % MAX_ROW_COUNT_PER_SHEET == 0 ? MAX_ROW_COUNT_PER_SHEET : totalCount % MAX_ROW_COUNT_PER_SHEET;
            // 计算最后一 Sheet 需要写入的次数
            long lastSheetWriteCount = lastSheetRowCount % MAX_QUERY_WRITE_COUNT_PER_SHEET == 0 ? (lastSheetRowCount / MAX_QUERY_WRITE_COUNT_PER_SHEET) : (lastSheetRowCount / MAX_QUERY_WRITE_COUNT_PER_SHEET + 1);
            for (int i = 0; i < sheetNum; i++) {
                // 创建 ExcelWriterSheetBuilder 对象
                ExcelWriterSheetBuilder writerSheetBuilder = buildExcelWriterSheet(i, sheetName + (i + 1), clazz, needMerge, needCustomStyle);
                // 循环写入次数: 自增条件是当不是最后一个 Sheet 的时候写入次数为正常的每个 Sheet 写入的次数，如果是最后一个就需要使用计算的次数 lastSheetWriteCount
                for (int j = 0; j < (i != sheetNum - 1 ? sheetWriteCount : lastSheetWriteCount); j++) {
                    // 读取当前批次的数据
                    List<T> writeList = pageQuery.apply(j + 1 + sheetWriteCount * i, MAX_QUERY_WRITE_COUNT_PER_SHEET);
                    if (!writeList.isEmpty()) {
                        excelWriter.write(writeList, writerSheetBuilder.build());
                    }
                    // TODO 深度分页问题
                }
            }
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "utf-8"));
        } catch (Exception e) {
            log.error("导出{}文件失败：", fileName, e);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JsonUtil.objectMapper.writeValueAsString(ResponseVO.error()));
        } finally {
            Optional.ofNullable(excelWriter).ifPresent(ExcelWriter::finish);
        }
    }

    /**
     * 构建 ExcelWriterSheet
     *
     * @param sheetNum        单元号
     * @param sheetName       单元名
     * @param clazz           数据类型
     * @param needMerge       是否合并单元格
     * @param needCustomStyle 是否自定义样式设置
     * @return {@link ExcelWriterSheetBuilder}
     */
    private static ExcelWriterSheetBuilder buildExcelWriterSheet(Integer sheetNum, String sheetName, Class<?> clazz, Boolean needMerge, Boolean needCustomStyle) {
        ExcelWriterSheetBuilder writerSheetBuilder = EasyExcel.writerSheet(sheetNum, sheetName).head(clazz);
        if (Boolean.TRUE.equals(needMerge)) {
            writerSheetBuilder.registerWriteHandler(new ExcelCellMergeStrategy(clazz));
        }
        if (Boolean.TRUE.equals(needCustomStyle)) {
            writerSheetBuilder.registerWriteHandler(customStyleStrategy());
        }
        return writerSheetBuilder;
    }

    /**
     * 自定义样式
     *
     * @return 样式策略
     */
    private static HorizontalCellStyleStrategy customStyleStrategy() {
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
     * 计算 Sheet 数量
     *
     * @param totalCount 总数量
     * @param maxCount   允许最大数量
     * @return Sheet 数量
     */
    private static int calculateSheetNum(int totalCount, int maxCount) {
        if (totalCount == 0 || maxCount == 0) {
            return 0;
        }
        if (totalCount < maxCount) {
            return 1;
        }
        return totalCount % maxCount == 0 ? (totalCount / maxCount) : (totalCount / maxCount + 1);
    }
}
