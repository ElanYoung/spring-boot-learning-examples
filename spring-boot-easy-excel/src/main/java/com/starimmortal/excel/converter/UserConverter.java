package com.starimmortal.excel.converter;

import com.starimmortal.excel.dto.UserExcelDTO;
import com.starimmortal.excel.pojo.UserDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author william@StarImmortal
 * @date 2023/03/21
 */
@Mapper
public interface UserConverter {

    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserExcelDTO convert(UserDO user);

    UserDO convert(UserExcelDTO userExcel);

    List<UserDO> convert(List<UserExcelDTO> userExcelList);
}
