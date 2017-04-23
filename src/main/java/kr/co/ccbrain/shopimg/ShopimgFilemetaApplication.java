package kr.co.ccbrain.shopimg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@ComponentScan(basePackages = "kr.co.ccbrain.shopimg")
//@PropertySource("config.properties")
@PropertySource("file:${shopimg.config}/config.properties")
// -Dshopimg.config=rootDir
@MapperScan(value = { "kr.co.ccbrain.shopimg.mapper" })
public class ShopimgFilemetaApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(ShopimgFilemetaApplication.class, args);
		Main mainObj = ctx.getBean(Main.class);
		mainObj.init();
	}
}
