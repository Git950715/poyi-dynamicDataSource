/**
 * Copyright (C), 2020
 * FileName: DataSourceConfig
 * Author:   HDC
 * Date:     2020/6/3 18:24
 * Description: 数据源基础配置类
 */
package com.poyi.springboot.configure;

import com.poyi.springboot.creator.BasicDataSourceCreator;
import com.poyi.springboot.creator.DataSourceCreator;
import com.poyi.springboot.creator.HikariDataSourceCreator;
import com.poyi.springboot.provider.DynamicDataSourceProvider;
import com.poyi.springboot.provider.YmlDynamicDataSourceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈数据源基础配置类〉
 *
 * @author Administrator
 * @create 2020/6/3
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnProperty(prefix = DynamicDataSourceProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataSourceConfig {

    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public DynamicDataSourceProvider dynamicDataSourceProvider() {
        Map<String, DataSourceProperty> datasourceMap = properties.getDatasource();
        return new YmlDynamicDataSourceProvider(datasourceMap);
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource(DynamicDataSourceProvider dynamicDataSourceProvider) {
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<String,DataSource> dataSourceMap = dynamicDataSourceProvider.loadDataSources();
        Map<Object, Object> targetDataSources = new HashMap<>();
        Object defaultTargetDataSource = null;
        for (Map.Entry<String, DataSource> dsItem : dataSourceMap.entrySet()) {
            if(dsItem.getKey().contains("master")){
                defaultTargetDataSource = dsItem.getValue();
            }
            System.out.println(dsItem.getKey()+"||"+dsItem.getValue());
            targetDataSources.put(dsItem.getKey(), dsItem.getValue());
        }
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        return dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DataSourceCreator dataSourceCreator() {
        DataSourceCreator dataSourceCreator = new DataSourceCreator();
        dataSourceCreator.setBasicDataSourceCreator(basicDataSourceCreator());
        dataSourceCreator.setHikariDataSourceCreator(hikariDataSourceCreator());
        return dataSourceCreator;
    }

    @Bean
    @ConditionalOnMissingBean
    public BasicDataSourceCreator basicDataSourceCreator() {
        return new BasicDataSourceCreator();
    }

    @Bean
    @ConditionalOnMissingBean
    public HikariDataSourceCreator hikariDataSourceCreator() {
        return new HikariDataSourceCreator(properties.getHikari());
    }


}
