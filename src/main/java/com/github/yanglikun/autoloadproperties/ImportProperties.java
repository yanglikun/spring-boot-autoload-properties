package com.github.yanglikun.autoloadproperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动导入properties
 * Created by yanglikun on 2017/3/19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ImportProperties {

    /**
     * properties配置文件的位置
     *
     * @return
     */
    String[] locations();
}
