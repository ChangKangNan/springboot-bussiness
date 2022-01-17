package com.example.springbootbussiness;

import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.example.springbootbussiness")
@EnableDubbo
@NacosPropertySource(dataId = "bussiness", autoRefreshed = true)
public class SpringbootBussinessApplication {

    static Logger logger= LoggerFactory.getLogger(SpringbootBussinessApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBussinessApplication.class, args);
    }

    /**
     * 监听Nacos加载
     *
     * @param config
     */
    @NacosConfigListener(dataId = "bussiness", type = ConfigType.TEXT)
    public void onMessage(String config) {
        String[] propertiesLines = config.split("\\r?\\n");
        for (String properties: propertiesLines) {
         logger.info(properties);
        }
    }

}
