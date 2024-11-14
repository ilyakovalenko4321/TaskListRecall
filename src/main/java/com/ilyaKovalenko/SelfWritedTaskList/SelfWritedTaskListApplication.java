package com.ilyaKovalenko.SelfWritedTaskList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class SelfWritedTaskListApplication {

	public static void main(String[] args) {
		SpringApplication.run(SelfWritedTaskListApplication.class, args);
	}

}
