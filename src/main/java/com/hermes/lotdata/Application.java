package com.hermes.lotdata;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.beicai.common.annotation.ExcludeComponent;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
})
@ServletComponentScan
@EnableScheduling
@EnableFeignClients(basePackageClasses = Application.class, basePackages = {"com.hermes.lotdata"})
@ComponentScan(basePackages = {"com.hermes.lotdata"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {ExcludeComponent.class})})
@MapperScan({"com.hermes.lotdata.repository"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
