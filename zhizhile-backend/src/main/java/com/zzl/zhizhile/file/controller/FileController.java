package com.zzl.zhizhile.file.controller;

import com.zzl.zhizhile.common.constant.ErrorCode;
import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.file.mapper.FileResourceMapper;
import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件访问接口。
 */
@RestController
@RequestMapping("/api/files")
public class FileController {
    private final FileResourceMapper fileResourceMapper;

    public FileController(FileResourceMapper fileResourceMapper) {
        this.fileResourceMapper = fileResourceMapper;
    }

    /**
     * 按文件 ID 访问文件内容。
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<Resource> getFile(@PathVariable Long fileId) {
        FileResourceEntity file = fileResourceMapper.findById(fileId)
                .orElseThrow(() -> new BizException(ErrorCode.FILE_NOT_FOUND));
        Path path = Path.of(file.getRelativePath());
        if (!Files.exists(path)) {
            throw new BizException(ErrorCode.FILE_NOT_FOUND);
        }
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.inline().filename(file.getOriginalName()).build().toString())
                .contentType(MediaType.parseMediaType(file.getContentType() == null
                        ? MediaType.APPLICATION_OCTET_STREAM_VALUE : file.getContentType()))
                .body(resource);
    }
}
