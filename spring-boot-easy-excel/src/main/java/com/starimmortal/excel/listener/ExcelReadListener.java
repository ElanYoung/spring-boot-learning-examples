package com.starimmortal.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.starimmortal.excel.exception.ExcelDataValidateException;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * EasyExcel读取监听器
 *
 * @author william@StarImmortal
 * @date 2022/03/09
 */
@Slf4j
public class ExcelReadListener<T> implements ReadListener<T> {

	/**
	 * 每隔 batchSize 条更新数据库，然后清理缓存数据，方便内存回收
	 */
	private final Integer batchSize;

	/**
	 * 数据消费者
	 */
	private final Consumer<List<T>> consumer;

	/**
	 * 校验器
	 */
	private final Validator validator;

	/**
	 * 字段所对应列号集合
	 */
	private final Map<String, Integer> columnIndexMap;

	/**
	 * 初始化缓存数据列表
	 */
	private List<T> cachedDataList;

	public ExcelReadListener(final Consumer<List<T>> consumer) {
		this(1000, consumer);
	}

	public ExcelReadListener(final Integer batchSize, final Consumer<List<T>> consumer) {
		this.batchSize = batchSize;
		this.consumer = consumer;
		this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		this.columnIndexMap = new HashMap<>(16);
		this.cachedDataList = ListUtils.newArrayListWithExpectedSize(batchSize);
	}

	@Override
	public void invoke(T data, AnalysisContext context) {
		this.validateExcelData(data, context);
		this.cachedDataList.add(data);
		// 达到 batchSize 了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
		if (this.cachedDataList.size() >= this.batchSize) {
			this.consumer.accept(this.cachedDataList);
			// 存储完成清理
			this.cachedDataList = ListUtils.newArrayListWithExpectedSize(this.batchSize);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		log.debug("Excel read analysed");
		if (!this.cachedDataList.isEmpty()) {
			this.consumer.accept(this.cachedDataList);
			this.cachedDataList.clear();
		}
		this.columnIndexMap.clear();
	}

	/**
	 * 校验Excel数据
	 * @param data Excel数据
	 * @param context 上下文
	 */
	private void validateExcelData(T data, AnalysisContext context) {
		Optional<ConstraintViolation<T>> violationOptional = this.validator.validate(data, Default.class)
			.stream()
			.findFirst();
		violationOptional.ifPresent(violation -> {
			String field = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			Integer sheetNo = context.readSheetHolder().getSheetNo() + 1;
			Integer rowIndex = context.readRowHolder().getRowIndex();
			Integer columnIndex = getColumnIndex(field, context) + 1;
			throw new ExcelDataValidateException(sheetNo, rowIndex, columnIndex, message);
		});
	}

	/**
	 * 获取列号
	 * @param field 字段
	 * @param context 上下文
	 * @return 列号
	 */
	private Integer getColumnIndex(String field, AnalysisContext context) {
		if (!this.columnIndexMap.isEmpty()) {
			return this.columnIndexMap.get(field);
		}
		Map<Integer, Head> headMap = context.readSheetHolder().excelReadHeadProperty().getHeadMap();
		for (Map.Entry<Integer, Head> entry : headMap.entrySet()) {
			this.columnIndexMap.put(entry.getValue().getFieldName(), entry.getKey());
		}
		return this.columnIndexMap.get(field);
	}

}
