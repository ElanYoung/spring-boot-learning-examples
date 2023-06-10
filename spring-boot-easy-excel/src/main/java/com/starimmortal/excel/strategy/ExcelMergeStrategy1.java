package com.starimmortal.excel.strategy;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.starimmortal.excel.annotation.ExcelMerge;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义单元格合并策略
 *
 * @author william@StarImmortal
 */
public class ExcelMergeStrategy1 implements RowWriteHandler {

    /**
     * 主键下标
     */
    private Integer primaryKeyIndex;

    /**
     * 需要合并的列的下标集合
     */
    private final List<Integer> mergeColumnIndexList = new ArrayList<>();

    /**
     * 数据类型
     */
    private final Class<?> elementType;

    public ExcelMergeStrategy1(Class<?> elementType) {
        this.elementType = elementType;
    }

    @Override
    public void afterRowDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Integer relativeRowIndex, Boolean isHead) {
        // 判断是否为标题
        if (isHead) {
            return;
        }
//        // 当前行
//        int currentRowIndex = row.getRowNum();
//        // 每一行最大列数
//        short lastCellNum = row.getLastCellNum();
        // 获取当前工作表
        Sheet sheet = writeSheetHolder.getSheet();
        // 初始化主键下标和需要合并字段的下标
        if (null == primaryKeyIndex) {
            this.initPrimaryIndexAndMergeIndex(writeSheetHolder);
        }
        // 判断是否需要和上一行进行合并
        // 不能和标题合并，只能数据行之间合并
        if (row.getRowNum() <= 1) {
            return;
        }
        // 获取上一行数据
        Row lastRow = sheet.getRow(row.getRowNum() - 1);
        // 将本行和上一行是同一类型的数据（通过主键字段进行判断），则需要合并
        if (lastRow.getCell(primaryKeyIndex).getStringCellValue().equalsIgnoreCase(row.getCell(primaryKeyIndex).getStringCellValue())) {
            for (Integer mergeIndex : mergeColumnIndexList) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum() - 1, row.getRowNum(), mergeIndex, mergeIndex);
                sheet.addMergedRegionUnsafe(cellRangeAddress);
            }
        }
    }

    /**
     * 初始化主键下标和需要合并字段的下标
     *
     * @param writeSheetHolder WriteSheetHolder
     */
    private void initPrimaryIndexAndMergeIndex(WriteSheetHolder writeSheetHolder) {
        // 获取当前工作表
        Sheet sheet = writeSheetHolder.getSheet();
        // 获取标题行
        Row titleRow = sheet.getRow(0);
        // 获取所有属性字段
        Field[] fields = this.elementType.getDeclaredFields();
        // 遍历所有字段
        for (Field field : fields) {
            // 获取@ExcelProperty注解，用于获取该字段对应列的下标
            ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
            // 判断是否为空
            if (null == excelProperty) {
                continue;
            }
            // 获取自定义注解，用于合并单元格
            ExcelMerge excelMerge = field.getAnnotation(ExcelMerge.class);
            // 判断是否需要合并
            if (null == excelMerge) {
                continue;
            }
            for (int index = 0; index < fields.length; index++) {
                Cell cell = titleRow.getCell(index);
                if (null == cell) {
                    continue;
                }
                // 将字段和表头匹配上
                if (excelProperty.value()[0].equalsIgnoreCase(cell.getStringCellValue())) {
                    if (excelMerge.isPrimaryKey()) {
                        primaryKeyIndex = index;
                    }
                    if (excelMerge.merge()) {
                        mergeColumnIndexList.add(index);
                    }
                }
            }
        }

        // 没有指定主键，则异常
        if (null == this.primaryKeyIndex) {
            throw new IllegalStateException("使用@ExcelMerge注解必须指定主键");
        }
    }

    private void mergeSheet(int firstRow, int lastRow, Sheet sheet) {
        for (Integer mergeIndex : mergeColumnIndexList) {
            CellRangeAddress cellRangeAddress = new CellRangeAddress(firstRow, lastRow, mergeIndex, mergeIndex);
            sheet.addMergedRegionUnsafe(cellRangeAddress);
        }
    }
}
