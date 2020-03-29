package com.douweikang.demo.demorpc;

import com.douweikang.demo.demorpc.common.service.UserInfoService;
import com.douweikang.demo.demorpc.microservice.nio.Providers;
import com.douweikang.demo.demorpc.microservice.provider.NettyProvider;
import com.douweikang.demo.demorpc.singleton.RPCInstances;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.douweikang.demo.demorpc.common.dao"})
public class MainProvider implements CommandLineRunner {

    @Autowired
    private UserInfoService userInfoService;

    public static void main(String[] args) {
        SpringApplication.run(MainProvider.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Providers providers = new Providers(userInfoService);

        NettyProvider nettyProvider = new NettyProvider(RPCInstances.Instance.getPort(), providers);

        new Thread(nettyProvider).start();
    }
}
