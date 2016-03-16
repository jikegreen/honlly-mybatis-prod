package com.honlly.dialect;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.util.LinkedCaseInsensitiveMap;

import com.honlly.commons.Entity;
import com.honlly.commons.PagingSqlBuilder;
import com.honlly.sqlbuilder.OracleSimpleSqlBuilder;
import com.honlly.sqlbuilder.SimpleSqlBuilder;

/**
 * Oracle方言
 * 
 * @created 2014年5月14日
 * @author wangk
 */
public class OracleDialect extends Dialect {
	/** JDBC类型映射 */
	private static final Map<Integer, String> jdbcTypeMapping;
	/** 数据库类型映射 */
	private static final Map<String, Integer> dbTypeMapping;
	
	static {
		Map<Integer, String> jdbcMap = new HashMap<Integer, String>();
		jdbcMap.put(Types.CHAR, "CHAR");
		jdbcMap.put(Types.VARCHAR, "VARCHAR2");
		jdbcMap.put(Types.LONGVARCHAR, "LONG");
		jdbcMap.put(Types.NUMERIC, "NUMBER");
		jdbcMap.put(Types.DECIMAL, "NUMBER");
		jdbcMap.put(Types.BIT, "NUMBER");
		jdbcMap.put(Types.TINYINT, "NUMBER");
		jdbcMap.put(Types.SMALLINT, "NUMBER");
		jdbcMap.put(Types.INTEGER, "NUMBER");
		jdbcMap.put(Types.BIGINT, "NUMBER");
		jdbcMap.put(Types.REAL, "NUMBER");
		jdbcMap.put(Types.FLOAT, "NUMBER");
		jdbcMap.put(Types.DOUBLE, "NUMBER");
		jdbcMap.put(Types.DATE, "DATE");
		jdbcMap.put(Types.TIME, "DATE");
		jdbcMap.put(Types.TIMESTAMP, "TIMESTAMP");
		jdbcMap.put(Types.BINARY, "RAW");
		jdbcMap.put(Types.VARBINARY, "RAW");
		jdbcMap.put(Types.LONGVARBINARY, "LONGRAW");
		jdbcMap.put(Types.BLOB, "BLOB");
		Map<String, Integer> dbMap = new LinkedCaseInsensitiveMap<Integer>();
		dbMap.put("CHAR", Types.CHAR);
		dbMap.put("VARCHAR2", Types.VARCHAR);
		dbMap.put("LONG", Types.LONGVARCHAR);
		dbMap.put("NUMBER", Types.NUMERIC);
		dbMap.put("DATE", Types.DATE);
		dbMap.put("TIMESTAMP", Types.TIMESTAMP);
		dbMap.put("RAW", Types.BINARY);
		dbMap.put("LONGRAW", Types.LONGVARBINARY);
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
		String pagingSql = SELECT + SPACE + "T.*" + COMMA + SPACE + "ROWNUM" + SPACE + AS + SPACE + "ROW_NUM" + 
				SPACE + FROM + SPACE + LEFT_BRACE + sql + RIGHT_BRACE + SPACE + AS + SPACE + "T" + 
				WHERE + SPACE + "ROWNUM" + SPACE + LT + SPACE + (offset + limit);
		if (offset == 0) {
			return pagingSql;
		}
		return SELECT + SPACE + ASTERISK + SPACE + FROM + SPACE + LEFT_BRACE + pagingSql + RIGHT_BRACE + 
				SPACE + AS + SPACE + "T_O" + SPACE + WHERE + SPACE + "ROW_NUM" + SPACE + GE + SPACE + offset;
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
				.append("t.table_name").append(SPACE).append(AS).append(SPACE).append(META_TABLE_NAME).append(COMMA).append(SPACE)
				.append("t.column_name").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_NAME).append(COMMA).append(SPACE)
				//DATA_LENGTH 长度 DATA_PRECISION 精度   DATA_SCALE 小数点后位数
				.append("t.data_type").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_TYPE).append(COMMA).append(SPACE)
				.append("t.nullable").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_NULLABLE).append(COMMA).append(SPACE)
//				.append("t.data_default").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_DEFAULT).append(COMMA).append(SPACE)
				.append("t.column_id").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_KEY).append(COMMA).append(SPACE)
				.append("c.comments").append(SPACE).append(AS).append(SPACE).append(META_COLUMN_COMMENT).append(SPACE)
				.append(FROM).append(SPACE).append("user_tab_columns").append(SPACE).append("t").append(SPACE)
				.append(JOIN).append(SPACE).append("user_col_comments").append(SPACE).append("c").append(SPACE)
				.append(ON).append(SPACE).append("t.table_name").append(SPACE).append(EQ).append(SPACE).append("c.table_name").append(SPACE)
				.append(AND).append(SPACE).append("t.column_name").append(SPACE).append(EQ).append(SPACE).append("c.column_name").append(SPACE)
				.append(ORDER_BY).append(SPACE).append("t.table_name");
		return sqlBuilder.toString();
	}

	@Override
	public SimpleSqlBuilder<? extends Entity> getSimpleSqlBuilder(Class<? extends Entity> clazz) {
		if(clazz == Entity.class) {
			return null;
		}
		SimpleSqlBuilder<? extends Entity> simpleSqlBuilder = (SimpleSqlBuilder<? extends Entity>)SimpleSqlBuilder.builderCache.get(clazz);
		if(simpleSqlBuilder == null) {
			simpleSqlBuilder = new OracleSimpleSqlBuilder<>(clazz);
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
		BigDecimal curseq = (BigDecimal) seq;
		return curseq.longValue();
	}

	@Override
	public void setMultiEntitiesIds(Collection<Entity> entities, Long curSeq) {
		Iterator<Entity> it = entities.iterator();
		Long start = curSeq - entities.size();
		while(it.hasNext()){
			Entity temp = it.next();
			temp.setId(++start);
		}
	}

}
