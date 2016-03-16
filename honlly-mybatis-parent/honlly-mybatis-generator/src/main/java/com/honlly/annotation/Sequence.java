/**
 * 
 */
package com.honlly.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @date 2015年8月21日 下午5:06:36 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Sequence {
	String value();
}
