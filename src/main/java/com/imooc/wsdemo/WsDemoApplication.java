package com.imooc.wsdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   //开启定时任务
@SpringBootApplication
public class WsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(WsDemoApplication.class, args);
	}

}
