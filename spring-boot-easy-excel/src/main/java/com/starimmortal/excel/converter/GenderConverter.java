package com.starimmortal.excel.converter;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.starimmortal.excel.enumeration.GenderEnum;

/**
 * Excel 性别转换器
 *
 * @author william@StarImmortal
 */
public class GenderConverter implements Converter<Integer> {

	@Override
	public Class<?> supportJavaTypeKey() {
		return Integer.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Integer convertToJavaData(ReadConverterContext<?> context) {
		return GenderEnum.convert(context.getReadCellData().getStringValue()).getValue();
	}

	@Override
	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) {
		return new WriteCellData<>(GenderEnum.convert(context.getValue()).getDescription());
	}

}
