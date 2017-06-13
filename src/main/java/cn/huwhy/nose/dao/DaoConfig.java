package cn.huwhy.nose.dao;

import java.io.IOException;
import java.util.Collections;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceArrayPropertyEditor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DaoConfig {

    @Bean
    public BasicDataSource dataSource(
            @Value("${driverClassName}") String driver,
            @Value("${url}") String url,
            @Value("${username}") String username,
            @Value("${password}") String password,
            @Value("${defaultAutoCommit}") Boolean defaultAutoCommit,
            @Value("${maxActive}") Integer maxActive,
            @Value("${maxIdle}") Integer maxIdle,
            @Value("${maxWait}") Long maxWait
    ) throws IOException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxIdle(maxIdle);
        dataSource.setMaxWait(maxWait);
        dataSource.setValidationQuery("select 1");
        dataSource.setConnectionInitSqls(Collections.singletonList("set names utf8mb4"));
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setTimeBetweenEvictionRunsMillis(3600000L);
        dataSource.setMinEvictableIdleTimeMillis(18000000L);
        return dataSource;
    }

    @Bean(name = "sqlSessionFactoryBeanName")
    @Autowired
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) {
        SqlSessionFactoryBean sqlFactory = new SqlSessionFactoryBean();
        sqlFactory.setDataSource(dataSource);
        sqlFactory.setTypeAliasesPackage("cn.huwhy.nose.model");
        sqlFactory.setConfigLocation(new ClassPathResource("cn/huwhy/nose/dao/sqlmap/sqlmap.xml"));
        ResourceArrayPropertyEditor editor = new ResourceArrayPropertyEditor();
        editor.setValue(null);
        editor.setAsText("classpath:cn/huwhy/nose/dao/sqlmap/mapping/*-mapping.xml");
        sqlFactory.setMapperLocations((Resource[]) editor.getValue());
        return sqlFactory;
    }

    @Bean
    @Autowired
    public PlatformTransactionManager txManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
