package com.honlly.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	/** 字段名，默认为空字符串，表示字段名为所注解的属性的名称的以下划线连接各单词的小写格式 */
	public String value() default "";
	/** 是否有非空约束标志，默认false表示没有非空约束(ID字段除外)  */
	public boolean isNotNull() default false;

}
