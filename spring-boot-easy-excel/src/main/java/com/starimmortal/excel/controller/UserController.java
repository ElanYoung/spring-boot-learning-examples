package com.starimmortal.excel.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starimmortal.core.constant.StringConstant;
import com.starimmortal.core.util.DateUtil;
import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.excel.annotation.ExecutionTime;
import com.starimmortal.excel.dto.UserExcelDTO;
import com.starimmortal.excel.exception.ExcelDataValidateException;
import com.starimmortal.excel.functional.PageQuery;
import com.starimmortal.excel.listener.ExcelReadListener;
import com.starimmortal.excel.service.UserService;
import com.starimmortal.excel.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户前端控制器
 *
 * @author william@StarImmortal
 * @date 2023/03/11
 */
@RestController
@RequestMapping("/user")
@Validated
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @ExecutionTime
    @ApiOperation(value = "导入用户")
    @PostMapping("/import")
    public ResponseVO importUser(@RequestPart(value = "file") MultipartFile file) {
        try {
            ExcelUtil.importExcel(file.getInputStream(), UserExcelDTO.class, new ExcelReadListener<>(batchList -> userService.importUser(batchList)));
            return ResponseVO.success();
        } catch (ExcelDataValidateException e) {
            String message = String.format("工作表%d中第%d行第%d列数据异常：%s", e.getSheetNo(), e.getRowIndex(), e.getColumnIndex(), e.getMessage());
            return ResponseVO.error(message);
        } catch (Exception e) {
            return ResponseVO.error(String.format("导入失败：%s", e.getMessage()));
        }
    }

    @ExecutionTime
    @ApiOperation(value = "导出用户")
    @GetMapping("/export")
    public void exportUser(HttpServletResponse response) {
        List<UserExcelDTO> userList = userService.list()
                .stream()
                .map(UserExcelDTO::new)
                .collect(Collectors.toList());
        String fileName = "用户列表" + StringConstant.SNAKE_SEPARATOR + DateUtil.getCurrentDay(DateUtil.TIME_WITH_UNDERLINE);
        ExcelUtil.exportExcel(response, fileName, "用户列表", UserExcelDTO.class, userList, false, true);
    }

    @ExecutionTime
    @ApiOperation(value = "分页导出用户")
    @GetMapping("/page/export")
    public void exportUserPage(HttpServletResponse response) {
        // 获取数据总数（实际中需要根据查询条件进行统计即可）
        long totalCount = userService.count();
        // 判断数据是否为空
        if (totalCount == 0) {
            return;
        }
        PageQuery<UserExcelDTO> pageQuery = (current, size) -> userService.page(new Page<>(current, size), null)
                        .getRecords()
                        .stream()
                        .map(UserExcelDTO::new)
                        .collect(Collectors.toList());
        String fileName = "用户列表" + StringConstant.SNAKE_SEPARATOR + DateUtil.getCurrentDay(DateUtil.TIME_WITH_UNDERLINE);
        ExcelUtil.exportExcel(response, fileName, "用户列表", UserExcelDTO.class, totalCount, pageQuery, false, true);
    }
}
