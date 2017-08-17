package com.allinpay.bigdata.config;



import lombok.extern.slf4j.Slf4j;


import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

@Slf4j
@Configuration
@EnableTransactionManagement // 启注解事务管理，等同于xml配置方式的 <tx:annotation-driven />
@MapperScan("com.allinpay.bigdata.mapper")
public class MyBatisConfig implements TransactionManagementConfigurer, EnvironmentAware {

    @Autowired
    DataSource dataSource;

    private RelaxedPropertyResolver propertyResolver;

    public void setEnvironment(Environment environment) {
        this.propertyResolver = new RelaxedPropertyResolver(environment, "mybatis.");
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() {
        try {
            SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
            sessionFactory.setDataSource(dataSource);
            sessionFactory.setTypeAliasesPackage(propertyResolver
                    .getProperty("typeAliasesPackage"));
            sessionFactory
                    .setMapperLocations(new PathMatchingResourcePatternResolver()
                            .getResources(propertyResolver
                                    .getProperty("mapperLocations")));
            sessionFactory
                    .setConfigLocation(new DefaultResourceLoader()
                            .getResource(propertyResolver
                                    .getProperty("configLocation")));

            return sessionFactory.getObject();
        } catch (Exception e) {
        	log.error("Create sqlSessionFactory failed",e);
            log.warn("Could not configure mybatis session factory");
            return null;
        }
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
//    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

    //    @Bean
//    public SqlSessionFactory sqlSessionFactory() {
//        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//
//        bean.setDataSource(dataSource);
//        bean.setTypeAliasesPackage("com.mcp.example.model");
//
////        PageHelper pageHelper = new PageHelper();
////        Properties properties = new Properties();
////        properties.setProperty("reasonable", "true");
////        properties.setProperty("supportMethodsArguments", "true");
////        properties.setProperty("returnPageInfo", "check");
////        properties.setProperty("params", "count=countSql");
////        pageHelper.setProperties(properties);
//
//        // 添加插件
////        bean.setPlugins(new Interceptor[] { pageHelper });
//        // 添加xml目录
//        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
//        try {
//            bean.setMapperLocations(resolver.getResources("classpath:mybatis/*.xml"));
//            return bean.getObject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }

}
