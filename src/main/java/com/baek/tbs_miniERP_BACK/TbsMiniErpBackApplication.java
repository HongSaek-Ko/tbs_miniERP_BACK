package com.baek.tbs_miniERP_BACK;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.baek.tbs_miniERP_BACK.mapper")
public class TbsMiniErpBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(TbsMiniErpBackApplication.class, args);
	}

}
