package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {


    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传：{}", file);
        String originalFileName = file.getOriginalFilename();
        String suffix = ".jpg";
        if (originalFileName != null) {
            suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        }
        String fileName = UUID.randomUUID().toString() + suffix;
        String imgUrl = "http://localhost/media/" + fileName;
        try {
            file.transferTo(new java.io.File("D:\\JAVAlearning\\TakeOutProject\\repository\\" + fileName));
            return Result.success(imgUrl);
        } catch (IllegalStateException | IOException e) {
            log.error("上传图片失败：{}", e);
            return Result.error("上传图片失败");
        }
    }
}
