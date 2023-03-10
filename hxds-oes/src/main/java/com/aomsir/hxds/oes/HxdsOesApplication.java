package com.aomsir.hxds.oes;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
/**
 * @Author: Aomsir
 * @Date: 2023/1/4
 * @Description:
 * @Email: info@say521.cn
 * @GitHub: https://github.com/aomsir
 */


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan
@ComponentScan("com.aomsir.*")
@EnableDistributedTransaction
public class HxdsOesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HxdsOesApplication.class, args);
    }

}
