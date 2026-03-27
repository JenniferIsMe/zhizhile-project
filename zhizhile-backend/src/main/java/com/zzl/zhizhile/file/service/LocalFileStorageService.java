package com.zzl.zhizhile.file.service;

import com.zzl.zhizhile.common.constant.ErrorCode;
import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件系统存储实现。
 */
@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${zhizhile.upload.dir}")
    private String uploadDir;

    @Value("${zhizhile.upload.allowed-exts}")
    private String allowedExts;

    /**
     * 将上传文件保存到本地目录。
     */
    @Override
    public FileResourceEntity save(MultipartFile file) {
        if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
            throw new BizException(ErrorCode.FILE_INVALID);
        }
        String ext = ext(file.getOriginalFilename());
        Set<String> allowSet = Arrays.stream(allowedExts.split(","))
                .map(String::trim).map(String::toLowerCase).collect(Collectors.toSet());
        if (!allowSet.contains(ext.toLowerCase())) {
            throw new BizException(ErrorCode.FILE_INVALID, "不支持的文件类型");
        }
        try {
            Path dir = Path.of(uploadDir);
            Files.createDirectories(dir);
            String stored = UUID.randomUUID() + "." + ext;
            Path target = dir.resolve(stored);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            FileResourceEntity entity = new FileResourceEntity();
            entity.setOriginalName(file.getOriginalFilename());
            entity.setStoredName(stored);
            entity.setFileExt(ext);
            entity.setContentType(file.getContentType());
            entity.setFileSize(file.getSize());
            entity.setStorageType("LOCAL");
            entity.setRelativePath(target.toString());
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            return entity;
        } catch (IOException e) {
            throw new BizException(ErrorCode.FILE_INVALID, "文件保存失败");
        }
    }

    private String ext(String name) {
        int idx = name.lastIndexOf('.');
        if (idx < 0 || idx == name.length() - 1) {
            return "";
        }
        return name.substring(idx + 1);
    }
}
