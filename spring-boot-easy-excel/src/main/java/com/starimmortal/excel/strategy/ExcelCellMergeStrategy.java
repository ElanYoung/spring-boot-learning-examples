package com.starimmortal.excel.strategy;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.starimmortal.excel.annotation.ExcelMerge;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
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
public class ExcelCellMergeStrategy extends AbstractMergeStrategy {

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

    public ExcelCellMergeStrategy(Class<?> elementType) {
        this.elementType = elementType;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
        // 当前行
        int currentColumnIndex = cell.getColumnIndex();
        // 当前列
        int currentRowIndex = cell.getRowIndex();
        // 初始化主键下标和需要合并字段的下标
        if (primaryKeyIndex == null) {
            this.initPrimaryIndexAndMergeIndex(sheet);
        }
        // 判断该列是否需要合并
        if (!mergeColumnIndexList.contains(currentColumnIndex)) {
            return;
        }
        if (currentRowIndex > 1) {
            for (Integer mergeIndex : mergeColumnIndexList) {
                if (currentColumnIndex == mergeIndex) {
                    this.mergeRow(sheet, cell, currentRowIndex, currentColumnIndex);
                    break;
                }
            }
        }
    }

    /**
     * 初始化主键下标和需要合并字段的下标
     *
     * @param sheet 工作表
     */
    private void initPrimaryIndexAndMergeIndex(Sheet sheet) {
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
            for (int i = 0; i < fields.length; i++) {
                Cell cell = titleRow.getCell(i);
                if (null == cell) {
                    continue;
                }
                // 将字段和表头匹配上
                if (excelProperty.value()[0].equalsIgnoreCase(cell.getStringCellValue())) {
                    if (excelMerge.isPrimaryKey()) {
                        primaryKeyIndex = i;
                    }
                    if (excelMerge.merge()) {
                        mergeColumnIndexList.add(i);
                    }
                }
            }
        }
        // 没有指定主键，则异常
        if (null == this.primaryKeyIndex) {
            throw new IllegalStateException("使用@ExcelMerge注解必须指定主键");
        }
    }

    /**
     * 向上合并单元格
     *
     * @param sheet       工作表
     * @param cell        当前单元格
     * @param rowIndex    当前行
     * @param columnIndex 当前列
     */
    private void mergeRow(Sheet sheet, Cell cell, int rowIndex, int columnIndex) {
        // 获取当前单元格所在的行数据的唯一标识
        Object currentCellValue = getCellValue(cell);
        // 获取当前单元格的正上方的单元格所在的行数据的唯一标识
        Object previousCellValue = getCellValue(cell.getSheet().getRow(rowIndex - 1).getCell(columnIndex));
        // 判断两条数据的是否是同一集合，只有同一集合的数据才能合并单元格
        boolean cellDataEqual = previousCellValue.equals(currentCellValue);
        // 唯一标识列内容是否相同
        boolean uniqueCellDataEqual = true;
        if (columnIndex >= primaryKeyIndex) {
            // 获取当前单元格唯一标识数值
            Object currentUniqueCellValue = getCellValue(cell.getRow().getCell(primaryKeyIndex));
            // 获取当前单元格正上方的单元格唯一标识的数值
            Object previousUniqueCellValue = getCellValue(cell.getSheet().getRow(rowIndex - 1).getCell(primaryKeyIndex));
            // 判断唯一标识列内容是否相同
            uniqueCellDataEqual = currentUniqueCellValue.equals(previousUniqueCellValue);
        }

        // 合并条件：
        // 1. 将当前单元格数据与上一个单元格数据比较，相同则执行合并逻辑
        // 2. 唯一标识列内容相同，才能进行下一列合并
        if (!(cellDataEqual && uniqueCellDataEqual)) {
            return;
        }

        List<CellRangeAddress> mergedRegions = sheet.getMergedRegions();
        boolean isMerged = false;
        for (int i = 0; i < mergedRegions.size() && !isMerged; i++) {
            CellRangeAddress cellRange = mergedRegions.get(i);
            // 若上一个单元格已经被合并，则先移出原有的合并单元，再重新添加合并单元
            if (cellRange.isInRange(rowIndex - 1, columnIndex)) {
                sheet.removeMergedRegion(i);
                cellRange.setLastRow(rowIndex);
                sheet.addMergedRegion(cellRange);
                isMerged = true;
            }
        }
        // 若上一个单元格未被合并，则新增合并单元
        if (!isMerged) {
            CellRangeAddress cellRange = new CellRangeAddress(rowIndex - 1, rowIndex, columnIndex, columnIndex);
            sheet.addMergedRegion(cellRange);
        }
    }

    private Object getCellValue(Cell cell) {
        return CellType.STRING.equals(cell.getCellType()) ? cell.getStringCellValue() : cell.getNumericCellValue();
    }
}
