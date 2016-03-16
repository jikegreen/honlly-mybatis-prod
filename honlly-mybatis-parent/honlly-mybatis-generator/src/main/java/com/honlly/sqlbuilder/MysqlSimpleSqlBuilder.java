/**
 * 
 */
package com.honlly.sqlbuilder;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.executor.ExecutorException;

import com.honlly.commons.Entity;
import com.honlly.dialect.Dialect;

/**
 * @date 2015年8月17日 下午5:41:25 
 * @author wangk
 * @param <E>
 * @Description:
 * @project honlly-mybatis
 */
public class MysqlSimpleSqlBuilder<S extends Entity> extends SimpleSqlBuilder<S> {
	public MysqlSimpleSqlBuilder(Class<S> entityClass) {
		super(entityClass);
	}

	@Override
	protected String getInsertSql(Set<String> fieldSet, boolean isIncludeIdField) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		sb.append("INSERT INTO ").append(tableName).append("(");
		for (String field : fieldSet) {
			if(!fieldColumnMapping.containsKey(field) || field.equals(idField) && !isIncludeIdField) {
				continue;
			}
			sb.append(fieldColumnMapping.get(field)).append(", ");
			sb2.append("#{").append(field).append(",").append("jdbcType=").append(Dialect.get(fieldJdbcTypeMapping.get(field))).append("}").append(", ");
		}
		if(sb2.length() > 0) {
			sb.delete(sb.length()-2, sb.length());
			sb2.delete(sb2.length()-2, sb2.length());
		}
		sb.append(") VALUES(");
		sb.append(sb2);
		sb.append(")");
		String sql = sb.toString();
		log.debug(sql);
		return sql;
	}
	
	@Override
	public String getBatchInsertSql(List<Entity> entitys,Long... ids) {
		StringBuilder sb = new StringBuilder(INSERT_INTO + SPACE).append(tableName).append(LEFT_BRACE).append(idField);
		StringBuilder[] sb2s = new StringBuilder[entitys.size()];
		for (int i = 0; i < sb2s.length; i++) {
			if(!entitys.get(i).isTransient()) {
				throw new ExecutorException("The record " + entitys.get(i) + " is exist!");
			}
			sb2s[i] = new StringBuilder(LEFT_BRACE).append(i < ids.length ? "#{param2["+i+"]}" : "null");
		}
		for (Entry<String, String> entry : fieldColumnMapping.entrySet()) {
			String field = entry.getKey();
			if(field.equals(idField)) {
				continue;
			}
			sb.append(COMMA + SPACE).append(entry.getValue());
			for (int i = 0; i < sb2s.length; i++) {
				sb2s[i].append(COMMA + SPACE).append("#{param1["+i+"].").append(field).append(",").append("jdbcType=").append(Dialect.get(fieldJdbcTypeMapping.get(field))).append("}");
			}
		}
		sb.append(RIGHT_BRACE + SPACE + VALUES);
		for (int i = 0; i < sb2s.length; i++) {
			sb.append(sb2s[i]).append(RIGHT_BRACE + COMMA + SPACE);
		}
		sb.delete(sb.length()-2, sb.length());
		return sb.toString();
	}
	
	@Override
	public String getCurrentSeqSql() {
		return "SELECT LAST_INSERT_ID() AS MAXID FROM DUAL";
	}
	
}
