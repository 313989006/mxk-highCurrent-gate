package com.mxk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.mxk.mapper")
@SpringBootApplication
public class MxkAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MxkAdminApplication.class, args);
    }

}
