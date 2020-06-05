/**
 * Copyright (C), 2020
 * FileName: DynamicDataSource
 * Author:   HDC
 * Date:     2020/6/3 19:26
 * Description:
 */
package com.poyi.springboot.configure;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;

/**
 * 〈〉
 *
 * @author Administrator
 * @create 2020/6/3
 * @since 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        System.out.println(DataSourceContextHolder.get());
        return DataSourceContextHolder.get();
    }

}
