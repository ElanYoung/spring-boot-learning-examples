package com.starimmortal.minio.controller;

import com.starimmortal.core.vo.ResponseVO;
import com.starimmortal.minio.configuration.MinioProperties;
import com.starimmortal.minio.util.FileUtil;
import com.starimmortal.minio.util.MinioUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author william@StarImmortal
 * @date 2022/09/04
 */
@RestController
@RequestMapping("/minio")
@Api(tags = "MinIO")
public class MinioController {

    @Autowired
    private MinioProperties minioProperties;

    @ApiOperation(value = "上传文件")
    @SneakyThrows
    @PostMapping(value = "/upload")
    public ResponseVO upload(@RequestParam(name = "file") MultipartFile multipartFile) {
        if (ObjectUtils.isEmpty(multipartFile)) {
            return ResponseVO.error("上传失败");
        }
        String extension = FileUtil.getFileExtension(multipartFile);
        String fileName = FileUtil.getNewFilename(extension);
        MinioUtil.createBucket(minioProperties.getBucket());
        MinioUtil.uploadFile(minioProperties.getBucket(), multipartFile, fileName);
        return ResponseVO.success("上传成功", MinioUtil.getPreSignedObjectUrl(minioProperties.getBucket(), fileName));
    }
}
