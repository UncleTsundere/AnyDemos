package com.douweikang.demo.demorpc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.douweikang.demo.demorpc.common.dao"})
public class DemoRpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoRpcApplication.class, args);
    }
}
