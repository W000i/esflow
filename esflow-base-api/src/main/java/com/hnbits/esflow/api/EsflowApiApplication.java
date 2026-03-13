package com.hnbits.esflow.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class })
@EnableTransactionManagement
@MapperScan(basePackages = { "com.hnbits.**.dao" })
@ComponentScan(basePackages = { "com.hnbits" })
@EnableCaching
@EnableAsync 
public class EsflowApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EsflowApiApplication.class, args);
	}

	/**
	 * 如此配置打包后可以war包才可在tomcat下使用
	 * 提交人：Test SecGuardSupport@asiainfo.com
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// 注意这里要指向原先用main方法执行的Application启动类
		return builder.sources(EsflowApiApplication.class);
	}

}
