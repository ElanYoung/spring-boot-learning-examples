package com.starimmortal.quartz.controller;

import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.quartz.dto.QuartzJobDTO;
import com.starimmortal.quartz.service.QuartzJobService;
import com.starimmortal.quartz.vo.QuartzJobVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务控制器
 *
 * @author william@StarImmortal
 * @date 2022/09/26
 */
@RestController
@RequestMapping("/quartz")
@Validated
@Api(tags = "定时任务")
public class QuartzJobController {

    @Autowired
    private QuartzJobService quartzJobService;

    @ApiOperation(value = "调度定时任务")
    @PostMapping("/schedule")
    public ResponseVO scheduleJob(@RequestBody @Validated QuartzJobDTO dto) {
        quartzJobService.scheduleJob(dto);
        return ResponseVO.success();
    }

    @ApiOperation(value = "重新调度定时任务")
    @PostMapping("/reschedule")
    public ResponseVO rescheduleJob(@RequestBody @Validated QuartzJobDTO dto) {
        quartzJobService.rescheduleJob(dto);
        return ResponseVO.success();
    }

    @ApiOperation(value = "暂停定时任务")
    @PostMapping("/pause")
    public ResponseVO pauseJob(@RequestBody @Validated QuartzJobDTO dto) {
        quartzJobService.pauseJob(dto.getJobName(), dto.getJobGroupName());
        return ResponseVO.success();
    }

    @ApiOperation(value = "恢复定时任务")
    @PostMapping("/resume")
    public ResponseVO resumeJob(@RequestBody @Validated QuartzJobDTO dto) {
        quartzJobService.resumeJob(dto.getJobName(), dto.getJobGroupName());
        return ResponseVO.success();
    }

    @ApiOperation(value = "删除定时任务")
    @PostMapping("/delete")
    public ResponseVO deleteJob(@RequestBody @Validated QuartzJobDTO dto) {
        quartzJobService.deleteJob(dto.getJobName(), dto.getJobGroupName());
        return ResponseVO.success();
    }

    @ApiOperation(value = "查询所有任务")
    @GetMapping("/list")
    public ResponseVO<List<QuartzJobVO>> listJobs() {
        List<QuartzJobVO> jobs = quartzJobService.listJobs();
        return ResponseVO.success(jobs);
    }
}
