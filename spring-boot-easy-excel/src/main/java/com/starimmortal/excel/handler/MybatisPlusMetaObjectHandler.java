package com.starimmortal.excel.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * Mybatis Plus 自动填充配置
 *
 * @author william@StarImmortal
 * @date 2023/02/08
 */
@Slf4j
public class MybatisPlusMetaObjectHandler implements MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {
		log.debug("mybatis plus start insert fill ....");
		this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
		this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		log.debug("mybatis plus start update fill ....");
		this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
	}

}
