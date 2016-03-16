/**
 * 
 */
package com.honlly;

import org.mybatis.spring.SqlSessionFactoryBean;

/**
 * 扩展数据库方言
 * @date 2016年3月15日 上午9:55:14 
 * @author wangk
 * @Description:
 * @project mybatis-generator
 */
public class SqlSessionFactoryBeanExtend extends SqlSessionFactoryBean {
	private String dialect;

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getDialect() {
		return dialect;
	}
}
