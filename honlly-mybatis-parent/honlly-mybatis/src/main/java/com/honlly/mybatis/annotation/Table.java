package com.honlly.mybatis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
	/** 表名，默认为空字符串，表示表名为所注解的类的名称的以下划线连接各单词的小写格式  */
	public String value() default "";

}
