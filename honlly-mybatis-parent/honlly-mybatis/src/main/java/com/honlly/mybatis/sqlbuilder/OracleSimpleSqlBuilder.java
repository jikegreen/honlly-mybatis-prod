/**
 * 
 */
package com.honlly.mybatis.sqlbuilder;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.ibatis.executor.ExecutorException;

import com.honlly.mybatis.Entity;
import com.honlly.mybatis.dialect.Dialect;

/**
 * @date 2015年8月21日 下午4:40:14 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class OracleSimpleSqlBuilder<S extends Entity> extends SimpleSqlBuilder<S> {
	
	public OracleSimpleSqlBuilder(Class<S> entityClass) {
		super(entityClass);
	}

	@Override
	protected String getInsertSql(Set<String> fieldSet, boolean isIncludeIdField) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		String seqName = getSequence();
		sb.append("INSERT INTO ").append(tableName).append("(");
		if(!fieldSet.contains(idField)){
			sb.append(idField).append(",");
			String nextval = getNextVal();
			sb2.append(nextval).append(",");
		}
		for (String field : fieldSet) {
			sb.append(fieldColumnMapping.get(field)).append(", ");
			if(seqName != null && field.equals(idField)){
				String nextval = getNextVal();
				sb2.append(nextval).append(",");
				continue;
			}
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
	public String getBatchInsertSql(List<Entity> entitys, Long... ids) {
		StringBuilder sb = new StringBuilder(INSERT_INTO).append(SPACE).append(tableName).append(LEFT_BRACE).append(idField);
		StringBuilder[] sb2 = new StringBuilder[entitys.size()];
		StringBuilder sb3 = new StringBuilder(LEFT_BRACE);
		for (int i = 0; i < sb2.length; i++) {
			if(!entitys.get(i).isTransient()) {
				throw new ExecutorException("The record " + entitys.get(i) + " is exist!");
			}
			sb2[i] = new StringBuilder(SPACE).append(SELECT).append(SPACE);
		}
		for (Entry<String, String> entry : fieldColumnMapping.entrySet()) {
			String field = entry.getKey();
			if(field.equals(idField)) {
				continue;
			}
			sb.append(COMMA + SPACE).append(entry.getValue());
			for (int i = 0; i < sb2.length; i++) {
				sb2[i].append("#{param1["+i+"].").append(field).append(",").append("jdbcType=").append(Dialect.get(fieldJdbcTypeMapping.get(field))).append("}").append(COMMA + SPACE);
			}
		}
		sb.append(RIGHT_BRACE + SPACE).append(SELECT).append(SPACE).append(getNextVal()).append(COMMA).append("t.*").append(SPACE).append(FROM).append(SPACE);
		for (int i = 0; i < sb2.length; i++) {
			sb2[i].delete(sb2[i].lastIndexOf(COMMA), sb2[i].length()).append(SPACE).append(FROM).append(SPACE).append("DUAL").append(SPACE).append(UNION_ALL);
			sb3.append(sb2[i]).append(SPACE);
		}
		sb3.delete(sb3.lastIndexOf(UNION_ALL), sb3.length()).append(RIGHT_BRACE).append(SPACE).append("t");
		return sb.append(sb3).toString();
	}
	
	@Override
	public String getCurrentSeqSql() {
		//只适用于Sequence无高速缓存的情况
		return "SELECT LAST_NUMBER-INCREMENT_BY AS MAXID FROM USER_SEQUENCES WHERE SEQUENCE_NAME='" + getSequence() + "'";
	}
	
	@Override
	public String getNextSeqSql() {
		return "SELECT " + getSequence() + ".NEXTVAL AS MAXID FROM DUAL";
	}
	
	private String getNextVal(){
		return getSequence() + ".NEXTVAL";
	}

}
