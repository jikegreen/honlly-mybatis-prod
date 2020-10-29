package com.honlly.sqlbuilder;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.honlly.annotation.Column;
import com.honlly.commons.Entity;
import com.honlly.commons.SqlComponent;
import com.honlly.entity.utils.DbFieldUtils;
import com.honlly.parser.EntityAnnotationParser;

/**
 * 简单sql构造抽象类
 * @date 2015年9月15日 下午8:30:16 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public abstract class SimpleSqlBuilder<E extends Entity> implements SqlComponent {
	/** 日志对象  */
	protected final Logger log = Logger.getLogger(getClass());
	public static final Map<Class<? extends Entity>, SimpleSqlBuilder<? extends Entity>> builderCache 
			= new ConcurrentHashMap<>();

	/** 实体类类型  */
	private Class<E> entityClass;
	/** 实体类注解解析器 */
	private EntityAnnotationParser entityAnnotationParser;
	/** 表名 */
	protected String tableName;
	/** ID属性  */
	protected String idField;
	/** ID列  */
	private String idColumn;
	/** 序列 **/
	private String sequence;
	/** 实体类属性和数据表字段的映射 */
	protected Map<String, String> fieldColumnMapping = new HashMap<>();
	/** 字段和数据库类型的映射 **/
	protected Map<String, Integer> fieldJdbcTypeMapping = new HashMap<>();

	@SuppressWarnings("unchecked")
	public SimpleSqlBuilder(Class<E> entityClass) {
		this.entityClass = entityClass;
		entityAnnotationParser = EntityAnnotationParser.getInstance(entityClass);
		tableName = entityAnnotationParser.getTableName();
		idField = entityAnnotationParser.getIdField().getName();
		idColumn = entityAnnotationParser.getFieldColumnMapping().get(entityAnnotationParser.getIdField());
		sequence = entityAnnotationParser.getSequence();
		for (Entry<Field, String> entry : entityAnnotationParser.getFieldColumnMapping().entrySet()) {
			fieldColumnMapping.put(entry.getKey().getName(), entry.getValue());
			Class<?> fieldType = entry.getKey().getType();
			if(fieldType instanceof Serializable){
				fieldJdbcTypeMapping.put(entry.getKey().getName(), DbFieldUtils.getFieldType((Class<? extends Serializable>)fieldType));
			}
		}
	}

	private String findByIdSql;
	private String queryAllSql;
	private String queryCountSql;
	private String deleteSql;
	private String insertSql;
	private String includeIdFieldInsertSql;
	private String updateSql;

	public String getFindByIdSql() {
		if(findByIdSql == null) {
			findByIdSql = getQueryAllSql() + " WHERE " + idColumn + " = " + SqlComponent.PARAMETER_PLACEHOLDER;
		}
		log.debug(findByIdSql);
		return findByIdSql;
	}

	public String getQueryAllSql() {
		if(queryAllSql == null) {
			queryAllSql = "SELECT * FROM " + tableName;
		}
		log.debug(queryAllSql);
		return queryAllSql;
	}

	public String getQueryCountSql() {
		if(queryCountSql == null) {
			queryCountSql = "SELECT COUNT(*) FROM " + tableName;
		}
		log.debug(queryCountSql);
		return queryCountSql;
	}

	public String getDeleteSql() {
		if(deleteSql == null) {
			deleteSql = "DELETE FROM " + tableName + " WHERE " + idColumn + " = " + SqlComponent.PARAMETER_PLACEHOLDER;
		}
		log.debug(deleteSql);
		return deleteSql;
	}

	public String getInsertSql() {
		if(insertSql == null) {
			insertSql = getInsertSql(fieldColumnMapping.keySet(), false);
		}
		return insertSql;
	}

	public String getInsertSql(Set<String> fieldSet) {
		return getInsertSql(fieldSet, false);
	}

	protected abstract String getInsertSql(Set<String> fieldSet, boolean isIncludeIdField);

	public String getIncludeIdFieldInsertSql() {
		if(includeIdFieldInsertSql == null) {
			includeIdFieldInsertSql = getInsertSql(fieldColumnMapping.keySet(), true);
		}
		return includeIdFieldInsertSql;
	}

	public String getIncludeIdFieldInsertSql(Set<String> fieldSet) {
		return getInsertSql(fieldSet, true);
	}

	public String getUpdateSql() {
		if(updateSql == null) {
			updateSql = getUpdateSql(fieldColumnMapping.keySet());
		}
		return updateSql;
	}

	public String getUpdateSql(Set<String> fieldSet) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ").append(tableName).append(" SET");
		boolean hasUpdateField = false;
		for (String field : fieldSet) {
			if(!fieldColumnMapping.containsKey(field) || field.equals(idField)) {
				continue;
			}
			hasUpdateField = true;
			sb.append(" ").append(fieldColumnMapping.get(field)).append(" = ").append("#{").append(field).append("}").append(",");
		}
		if(hasUpdateField) {
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append(" WHERE ").append(idColumn).append(" = ").append("#{").append(idField).append("}");
		String sql = sb.toString();
		log.debug(sql);
		return sql;
	}

	public String getConditionSql(Entity condition) {
		return getConditionSql(condition, null);
	}

	public String getConditionSql(Entity condition, String paramKey) {
		if(condition == null) {
			return "";
		}
		if(paramKey == null) {
			paramKey = "";
		} else if(!paramKey.isEmpty()) {
			paramKey += ".";
		}
		StringBuilder sb = new StringBuilder();
		for (Entry<Field, String> entry : entityAnnotationParser.getFieldColumnMapping().entrySet()) {
			String field = entry.getKey().getName();
			try {
				if(new PropertyDescriptor(field, entityClass).getReadMethod().invoke(condition) == null) {
					continue;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			sb.append(entry.getValue()).append(" = ").append("#{").append(paramKey).append(field).append("} AND ");
		}
		if(sb.length() > 0) {
			sb.delete(sb.length()-5, sb.length());
		}
		return sb.toString();
	}

	public Set<String> getSqlFieldSet(Entity entity) {
		Set<String> fieldSet = new HashSet<>();
		for (Entry<Field, Column> entry : entityAnnotationParser.getFieldAtColumnMapping().entrySet()) {
			String field = entry.getKey().getName();
			try {
				if(entry.getValue().isNotNull() && new PropertyDescriptor(field, entityClass).getReadMethod().invoke(entity) == null) {
					continue;
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			fieldSet.add(field);
		}
		return fieldSet;
	}
	
	public abstract String getBatchInsertSql(List<Entity> entitys,Long... ids);

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public String getSequence() {
		return sequence;
	}
	
	public abstract String getCurrentSeqSql();
	
	public String getNextSeqSql(){
		throw new RuntimeException("The database can not support sequence!!!");
	}

}
