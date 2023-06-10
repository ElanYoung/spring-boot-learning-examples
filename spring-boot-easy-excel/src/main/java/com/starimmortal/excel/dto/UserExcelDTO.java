package com.starimmortal.excel.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.starimmortal.excel.converter.GenderConverter;
import com.starimmortal.excel.pojo.UserDO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 用户 Excel 业务对象
 *
 * @author william@StarImmortal
 * @date 2023/01/01
 */
@Data
@NoArgsConstructor
public class UserExcelDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不可为空")
    @ExcelProperty("用户名")
    @ColumnWidth(20)
    private String username;

    /**
     * 密码
     */
    @ExcelIgnore
    private String password;

    /**
     * 昵称
     */
    @ExcelProperty("昵称")
    @ColumnWidth(20)
    private String nickname;

    /**
     * 生日
     */
    @ExcelProperty("生日")
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd")
    private Date birthday;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不可为空")
    @ExcelProperty("手机号")
    @ColumnWidth(20)
    private String phone;

    /**
     * 身高（米）
     */
    @ExcelProperty("身高（米）")
    @NumberFormat("#.##")
    @ColumnWidth(16)
    private Double height;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别", converter = GenderConverter.class)
    @ColumnWidth(10)
    private Integer gender;

    public UserExcelDTO(UserDO user) {
        BeanUtils.copyProperties(user, this);
    }
}
