package com.honlly.mybatis;

import org.apache.log4j.Logger;

import com.honlly.commons.domain.Pagination;
import com.honlly.mybatis.dialect.Dialect;

public class PagingSqlBuilder {
	/** log4j对象 */
	private static final Logger log = Logger.getLogger(PagingSqlBuilder.class);

	/** 数据库方言 */
	private final Dialect dialect;

	public PagingSqlBuilder(Dialect dialect) {
		this.dialect = dialect;
	}

	public Dialect getDialect() {
		return dialect;
	}

	public String getCountSql(String rawSql) {
		String countSql = "SELECT COUNT(*) AS TOTALS " + rawSql.substring(rawSql.toUpperCase().indexOf("FROM"));
		int orderIndex = countSql.toUpperCase().lastIndexOf("ORDER");
		if(orderIndex >= 0) {
			countSql = countSql.substring(0, orderIndex).trim();
		}
		log.debug(countSql);
		return countSql;
	}

	public String getGroupCountSql(String rawSql) {
		String groupCountSql =  "SELECT SUM(TOTALS) FROM (" + getCountSql(rawSql) + ") AS T";
		log.debug(groupCountSql);
		return groupCountSql;
	}

	public String getPagingSql(String rawSql, Pagination pagination) {
		if(pagination == null || pagination.isInvalid()) {
			log.debug(rawSql);
			return rawSql;
		}
		String pagingSql = dialect.getLimitString(rawSql, pagination.getSkip(), pagination.getLimit());
		log.debug(pagingSql);
		return pagingSql;
	}

}
