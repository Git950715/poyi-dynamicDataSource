/**
 * Copyright © 2018 organization baomidou
 * <pre>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <pre/>
 */
package com.poyi.springboot.creator;

import com.poyi.springboot.configure.DataSourceProperty;
import com.poyi.springboot.constants.DdConstants;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 数据源创建器
 *
 * @author TaoYu
 * @since 2.3.0
 */
@Slf4j
@Setter
public class DataSourceCreator {

    /**
     * 是否存在druid
     */
    private static Boolean druidExists = false;
    /**
     * 是否存在hikari
     */
    private static Boolean hikariExists = false;

    private BasicDataSourceCreator basicDataSourceCreator;
    private HikariDataSourceCreator hikariDataSourceCreator;
    private String globalPublicKey;

    static {
        try {
            Class.forName(DdConstants.DRUID_DATASOURCE);
            druidExists = true;
            log.debug("dynamic-datasource detect druid,Please Notice \n " +
                    "https://github.com/baomidou/dynamic-datasource-spring-boot-starter/wiki/Integration-With-Druid");
        } catch (ClassNotFoundException ignored) {
        }
        try {
            Class.forName(DdConstants.HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException ignored) {
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        DataSource dataSource;
        //如果是jndi数据源
        Class<? extends DataSource> type = dataSourceProperty.getType();
        if (type == null) {
            /*if (druidExists) {
                //TODO
                dataSource = createDruidDataSource(dataSourceProperty);
            } else */if (hikariExists) {
                dataSource = createHikariDataSource(dataSourceProperty);
            } else {
                dataSource = createBasicDataSource(dataSourceProperty);
            }
        } /*else if (DdConstants.DRUID_DATASOURCE.equals(type.getName())) {
            //TODO
            //dataSource = createDruidDataSource(dataSourceProperty);
        }*/ else if (DdConstants.HIKARI_DATASOURCE.equals(type.getName())) {
            dataSource = createHikariDataSource(dataSourceProperty);
        } else {
            dataSource = createBasicDataSource(dataSourceProperty);
        }
        return dataSource;
    }

    /**
     * 创建基础数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
        if (StringUtils.isEmpty(dataSourceProperty.getPublicKey())) {
            dataSourceProperty.setPublicKey(globalPublicKey);
        }
        return basicDataSourceCreator.createDataSource(dataSourceProperty);
    }

    /**
     * 创建Hikari数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     * @author 离世庭院 小锅盖
     */
    public DataSource createHikariDataSource(DataSourceProperty dataSourceProperty) {
        if (StringUtils.isEmpty(dataSourceProperty.getPublicKey())) {
            dataSourceProperty.setPublicKey(globalPublicKey);
        }
        return hikariDataSourceCreator.createDataSource(dataSourceProperty);
    }
}
