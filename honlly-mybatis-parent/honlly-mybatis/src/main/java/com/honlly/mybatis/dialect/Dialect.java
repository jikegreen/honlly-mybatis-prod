package com.honlly.mybatis.dialect;

import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.builder.BuilderException;

import com.honlly.mybatis.Entity;
import com.honlly.mybatis.PagingSqlBuilder;
import com.honlly.mybatis.SqlComponent;
import com.honlly.mybatis.sqlbuilder.SimpleSqlBuilder;

public abstract class Dialect implements SqlComponent {
	/**
	 * 表元数据键名常量
	 */
	public static final String META_TABLE_NAME = "TABLE_NAME";
	public static final String META_COLUMN_NAME = "COLUMN_NAME";
	public static final String META_COLUMN_TYPE = "COLUMN_TYPE";
	public static final String META_COLUMN_NULLABLE = "COLUMN_NULLABLE";
	public static final String META_COLUMN_DEFAULT = "COLUMN_DEFAULT";
	public static final String META_COLUMN_KEY = "COLUMN_KEY";
	public static final String META_COLUMN_COMMENT = "COLUMN_COMMENT";
	public static final String META_VALUE_COLUMN_NULLABLE_YES = "YES";
	public static final String META_VALUE_COLUMN_NULLABLE_TRUE = "true";
	public static final String META_VALUE_COLUMN_NULLABLE_ONE = "1";
	public static final String META_VALUE_COLUMN_NULLABLE = "Y";
	public static final String META_VALUE_COLUMN_KEY_PRIMARY = "PRI";
	public static final String META_VALUE_COLUMN_KEY_UNIQUE = "UNI";
	/** mybatis jdbc类型映射 **/
	protected static final Map<Integer, String> mybatisJdbcTypeMapping;
	
	static{
		Map<Integer, String> mybatisJdbcMap = new HashMap<Integer, String>();
		mybatisJdbcMap.put(Types.CHAR, "CHAR");
		mybatisJdbcMap.put(Types.VARCHAR, "VARCHAR");
		mybatisJdbcMap.put(Types.LONGVARCHAR, "LONGVARCHAR");
		mybatisJdbcMap.put(Types.NUMERIC, "NUMERIC");
		mybatisJdbcMap.put(Types.DECIMAL, "DECIMAL");
		mybatisJdbcMap.put(Types.BIT, "BIT");
		mybatisJdbcMap.put(Types.TINYINT, "TINYINT");
		mybatisJdbcMap.put(Types.SMALLINT, "SMALLINT");
		mybatisJdbcMap.put(Types.INTEGER, "INTEGER");
		mybatisJdbcMap.put(Types.BIGINT, "BIGINT");
		mybatisJdbcMap.put(Types.REAL, "REAL");
		mybatisJdbcMap.put(Types.FLOAT, "FLOAT");
		mybatisJdbcMap.put(Types.DOUBLE, "DOUBLE");
		mybatisJdbcMap.put(Types.DATE, "DATE");
		mybatisJdbcMap.put(Types.TIME, "TIME");
		mybatisJdbcMap.put(Types.TIMESTAMP, "TIMESTAMP");
		mybatisJdbcMap.put(Types.BINARY, "BINARY");
		mybatisJdbcMap.put(Types.VARBINARY, "VARBINARY");
		mybatisJdbcMap.put(Types.LONGVARBINARY, "LONGVARBINARY");
		mybatisJdbcMap.put(Types.BLOB, "BLOB");
		mybatisJdbcMap.put(Types.CLOB, "CLOB");
		mybatisJdbcTypeMapping = Collections.unmodifiableMap(mybatisJdbcMap);
	}

	public boolean supportsLimit() {
		return false;
	}

	public boolean supportsLimitOffset() {
		return supportsLimit();
	}

	public String getLimitString(String sql, int offset, int limit) {
		throw new BuilderException("Paged queries not supported by " + getClass().getName());
	}

	public String getDbType(int jdbcType) {
		throw new UnsupportedOperationException();
	}

	public int getJdbcType(String dbType) {
		throw new UnsupportedOperationException();
	}

	public String toSqlString() {
		throw new UnsupportedOperationException();
	}
	
	public abstract SimpleSqlBuilder<? extends Entity> getSimpleSqlBuilder(Class<? extends Entity> clazz);
	
	public abstract PagingSqlBuilder getPagingSqlBuilder();
	
	/**
	 * 获取mybatis的映射类型
	 * @Date:2015年9月10日
	 * @author wangk
	 * @param jdbcType jdbc类型
	 * @return
	 * @Description:
	 */
	public static String get(int jdbcType){
		return mybatisJdbcTypeMapping.get(jdbcType);
	}
	
	/**
	 * 序列类型转换
	 * @Date:2015年9月10日
	 * @author wangk
	 * @param seq 待转换序列号
	 * @return
	 * @Description:
	 */
	public Long convertSeq(Object seq){
		throw new RuntimeException("Please override this method to convert seq to Long!!!");
	}
	
	public String getCurrentSeqSql(Class<? extends Entity> clazz){
		SimpleSqlBuilder<? extends Entity> builder = getSimpleSqlBuilder(clazz);
		return builder.getCurrentSeqSql();
	}
	
	/**
	 * 批量回填id
	 * @Date:2015年9月10日
	 * @author wangk
	 * @param entities 要回填id的实体集合
	 * @param curSeq 当前的序列号
	 * @Description:
	 */
	public abstract void setMultiEntitiesIds(Collection<Entity> entities,Long curSeq);

}
