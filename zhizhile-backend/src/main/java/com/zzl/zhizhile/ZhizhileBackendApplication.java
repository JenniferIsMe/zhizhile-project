package com.zzl.zhizhile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zzl.zhizhile", annotationClass = org.apache.ibatis.annotations.Mapper.class)
public class ZhizhileBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhizhileBackendApplication.class, args);
    }
}
