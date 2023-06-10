package com.starimmortal.excel.exception;

import com.alibaba.excel.exception.ExcelDataConvertException;

/**
 * @author william@StarImmortal
 * @date 2023/03/16
 */
public class ExcelDataValidateException extends ExcelDataConvertException {
    /**
     * 单元号
     */
    private Integer sheetNo;

    /**
     * 数据校验异常构造函数
     *
     * @param rowIndex    行号
     * @param columnIndex 列号
     * @param message     错误消息
     */
    public ExcelDataValidateException(Integer rowIndex, Integer columnIndex, String message) {
        super(rowIndex, columnIndex, null, null, message);
    }

    /**
     * 数据校验异常构造函数
     *
     * @param rowIndex    行号
     * @param columnIndex 列号
     * @param message     错误消息
     */
    public ExcelDataValidateException(Integer sheetNo, Integer rowIndex, Integer columnIndex, String message) {
        super(rowIndex, columnIndex, null, null, message);
        this.sheetNo = sheetNo;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }
}
