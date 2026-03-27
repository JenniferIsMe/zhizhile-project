package com.zzl.zhizhile.file.service;

import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件存储服务接口。
 */
public interface FileStorageService {
    /**
     * 保存上传文件，并返回文件元数据。
     */
    FileResourceEntity save(MultipartFile file);
}
