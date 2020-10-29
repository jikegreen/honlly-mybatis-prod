package com.honlly.dialect;

import java.math.BigInteger;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.util.LinkedCaseInsensitiveMap;

import com.honlly.commons.Entity;
import com.honlly.commons.PagingSqlBuilder;
import com.honlly.sqlbuilder.MysqlSimpleSqlBuilder;
import com.honlly.sqlbuilder.SimpleSqlBuilder;

public class MySQLDialect extends Dialect {
	/** JDBC类型映射 */
	private static final Map<Integer, String> jdbcTypeMapping;
	/** 数据库类型映射 */
	private static final Map<String, Integer> dbTypeMapping;
	static {
		Map<Integer, String> jdbcMap = new HashMap<>();
		jdbcMap.put(Types.CHAR, "CHAR");
		jdbcMap.put(Types.VARCHAR, "VARCHAR");
		jdbcMap.put(Types.LONGVARCHAR, "TEXT");
		jdbcMap.put(Types.NUMERIC, "NUMERIC");
		jdbcMap.put(Types.DECIMAL, "DECIMAL");
		jdbcMap.put(Types.BIT, "BIT");
		jdbcMap.put(Types.TINYINT, "TINYINT");
		jdbcMap.put(Types.SMALLINT, "SMALLINT");
		jdbcMap.put(Types.INTEGER, "INT");
		jdbcMap.put(Types.BIGINT, "BIGINT");
		jdbcMap.put(Types.REAL, "REAL");
		jdbcMap.put(Types.FLOAT, "FLOAT");
		jdbcMap.put(Types.DOUBLE, "DOUBLE");
		jdbcMap.put(Types.DATE, "DATETIME");
		jdbcMap.put(Types.TIME, "TIME");
		jdbcMap.put(Types.TIMESTAMP, "TIMESTAMP");
		jdbcMap.put(Types.BINARY, "BINARY");
		jdbcMap.put(Types.VARBINARY, "VARBINARY");
		jdbcMap.put(Types.LONGVARBINARY, "VARBINARY");
		jdbcMap.put(Types.BLOB, "BLOB");
		Map<String, Integer> dbMap = new LinkedCaseInsensitiveMap<>();
		dbMap.put("CHAR", Types.CHAR);
		dbMap.put("VARCHAR", Types.VARCHAR);
		dbMap.put("TEXT", Types.LONGVARCHAR);
		dbMap.put("NUMERIC", Types.NUMERIC);
		dbMap.put("DECIMAL", Types.DECIMAL);
		dbMap.put("BIT", Types.BIT);
		dbMap.put("TINYINT", Types.TINYINT);
		dbMap.put("SMALLINT", Types.SMALLINT);
		dbMap.put("INT", Types.INTEGER);
		dbMap.put("INTEGER", Types.INTEGER);
		dbMap.put("BIGINT", Types.BIGINT);
		dbMap.put("REAL", Types.REAL);
		dbMap.put("FLOAT", Types.FLOAT);
		dbMap.put("DOUBLE", Types.DOUBLE);
		dbMap.put("DATETIME", Types.DATE);
		dbMap.put("TIME", Types.TIME);
		dbMap.put("TIMESTAMP", Types.TIMESTAMP);
		dbMap.put("BINARY", Types.BINARY);
		dbMap.put("VARBINARY", Types.VARBINARY);
		dbMap.put("BLOB", Types.BLOB);
		jdbcTypeMapping = Collections.unmodifiableMap(jdbcMap);
		dbTypeMapping = Collections.unmodifiableMap(dbMap);
	}

	public boolean supportsLimitOffset() {
		return true;
	}
	
    public boolean supportsLimit() {
        return true;   
    }  
    
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		return sql + SPACE + "LIMIT" + SPACE + offset + COMMA + SPACE + limit;
	}  

	@Override
	public String getDbType(int jdbcType) {
		return jdbcTypeMapping.get(jdbcType);
	}

	@Override
	public int getJdbcType(String dbType) {
		Integer ret = dbTypeMapping.get(dbType);
		if(ret == null) {
			return 0;
		}
		return ret;
	}
	
	@Override
	public String toSqlString() {
		StringBuilder sqlBuilder = new StringBuilder(SELECT).append(SPACE)
				.append("table_name").append(SPACE).append(AS).append(SPACE).append(META_TABLE_NAME).append(COMMA).append(SPACE)
				.append("column_name").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_NAME).append(COMMA).append(SPACE)
				//DATA_LENGTH 长度 DATA_PRECISION 精度   DATA_SCALE 小数点后位数
				.append("column_type").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_TYPE).append(COMMA).append(SPACE)
				.append("is_nullable").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_NULLABLE).append(COMMA).append(SPACE)
				.append("column_default").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_DEFAULT).append(COMMA).append(SPACE)
				.append("column_key").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_KEY).append(COMMA).append(SPACE)
				.append("column_comment").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_COMMENT).append(SPACE)
				.append(FROM).append(SPACE).append("information_schema.columns").append(SPACE)
				.append(WHERE).append(SPACE).append("table_schema").append(EQ).append(SPACE).append("database()").append(SPACE)
				.append(ORDER_BY).append(SPACE).append("table_name").append(COMMA).append("ordinal_position");
		return sqlBuilder.toString();
	}

	@Override
	public SimpleSqlBuilder<? extends Entity> getSimpleSqlBuilder(Class<? extends Entity> clazz) {
		if(clazz == Entity.class) {
			return null;
		}
		SimpleSqlBuilder<? extends Entity> simpleSqlBuilder = (SimpleSqlBuilder<? extends Entity>)SimpleSqlBuilder.builderCache.get(clazz);
		if(simpleSqlBuilder == null) {
			simpleSqlBuilder = new MysqlSimpleSqlBuilder<>(clazz);
			SimpleSqlBuilder.builderCache.put(clazz, simpleSqlBuilder);
		}
		return simpleSqlBuilder;
	}

	@Override
	public PagingSqlBuilder getPagingSqlBuilder() {
		return new PagingSqlBuilder(this);
	}
	
	@Override
	public Long convertSeq(Object seq){
		return ((BigInteger) seq).longValue();
	}

	@Override
	public void setMultiEntitiesIds(Collection<Entity> entities, Long curSeq) {
		Iterator<Entity> it = entities.iterator();
		while(it.hasNext()){
			Entity temp = it.next();
			temp.setId(curSeq++);
		}
	}

}
