package kr.co.ccbrain.shopimg.cmn;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
public class Config {
//	@Autowired
//	private Environment env;

	@Bean
	public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
//		System.out.println("getProperty "+env.getProperty("spring.datasource.username"));
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		Resource[] arrResource = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml");
		sqlSessionFactoryBean.setMapperLocations(arrResource);

		return sqlSessionFactoryBean.getObject();
	}
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dbDataSource() {
		return new DbcpInit();
	}
}
