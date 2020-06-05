/**
 * Copyright (C), 2020
 * FileName: DataSourceContextHolder
 * Author:   HDC
 * Date:     2020/6/3 19:18
 * Description: 存储当先线程的数据源
 */
package com.poyi.springboot.configure;

/**
 * 〈存储当先线程的数据源〉
 *
 * @author Administrator
 * @create 2020/6/3
 * @since 1.0.0
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> DYNAMIC_DATASOURCE_CONTEXT = new ThreadLocal<>();
    public static void set(String datasourceType) {
        DYNAMIC_DATASOURCE_CONTEXT.set(datasourceType);
    }
    public static String get() {
        return DYNAMIC_DATASOURCE_CONTEXT.get();
    }
    public static void clear() {
        DYNAMIC_DATASOURCE_CONTEXT.remove();
    }

}
