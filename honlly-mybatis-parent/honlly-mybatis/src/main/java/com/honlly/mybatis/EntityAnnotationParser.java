package com.honlly.mybatis;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.parsing.ParsingException;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.honlly.commons.utils.ClassUtils;
import com.honlly.mybatis.annotation.Column;
import com.honlly.mybatis.annotation.Id;
import com.honlly.mybatis.annotation.Sequence;
import com.honlly.mybatis.annotation.Table;

public class EntityAnnotationParser {
	private static String seq_prefix = "SEQ_";
	private static final Map<Class<? extends Entity>, EntityAnnotationParser> parserCache 
			= new ConcurrentHashMap<Class<? extends Entity>, EntityAnnotationParser>();

	public static EntityAnnotationParser getInstance(Class<? extends Entity> entityClass) {
		if(entityClass == Entity.class) {
			return null;
		}
		EntityAnnotationParser entityAnnotationParser = parserCache.get(entityClass);
		if(entityAnnotationParser == null) {
			entityAnnotationParser = new EntityAnnotationParser(entityClass);
			parserCache.put(entityClass, entityAnnotationParser);
		}
		return entityAnnotationParser;
	}

	private String tableName;
	private Field idField;
	private String sequenceName;
	private Map<String, Field> fieldMapping;
	private Map<Field, Column> fieldAtColumnMapping;
	private Map<Field, String> fieldColumnMapping;
	private Map<String, Field> columnFieldMapping;
	private Map<String, Field> getMethodFieldMapping;
	

	private EntityAnnotationParser(Class<? extends Entity> entityClass) {
		tableName = parseTableName(entityClass);
		ColumnResult columnResult = parseColumn(entityClass);
		idField = columnResult.idField;
		sequenceName = parseSeqName(entityClass);
		fieldMapping = Collections.unmodifiableMap(columnResult.fieldMapping);
		fieldAtColumnMapping = Collections.unmodifiableMap(columnResult.fieldAtColumnMapping);
		fieldColumnMapping = Collections.unmodifiableMap(columnResult.fieldColumnMapping);
		columnFieldMapping = Collections.unmodifiableMap(columnResult.columnFieldMapping);
		getMethodFieldMapping = Collections.unmodifiableMap(columnResult.getMethodFieldMapping);
	}

	public String getTableName() {
		return tableName;
	}

	public Field getIdField() {
		return idField;
	}

	public Map<String, Field> getFieldMapping() {
		return fieldMapping;
	}

	public Map<Field, Column> getFieldAtColumnMapping() {
		return fieldAtColumnMapping;
	}

	public Map<Field, String> getFieldColumnMapping() {
		return fieldColumnMapping;
	}

	public Map<String, Field> getColumnFieldMapping() {
		return columnFieldMapping;
	}

	private String parseTableName(Class<? extends Entity> entityClass) {
		Table annotation = entityClass.getAnnotation(Table.class);
		if(annotation == null) {
			throw new ParsingException("Annotation " + Table.class.getName() + " not found for " + entityClass.getName());
		}
		String tableName = annotation.value();
		if(tableName.equals(Entity.TABLE_NAME_DEFAULT)) {
			tableName = getDefaultTableName(entityClass);
		}
		return tableName;
	}
	
	private String parseSeqName(Class<? extends Entity> entityClass) {
		Sequence annotation = entityClass.getAnnotation(Sequence.class);
		if(annotation == null) {
			return null;
		}
		String seqName = annotation.value();
		if(seqName.equals(Entity.SEQ_NAME_DEFAULT)) {
			seqName = seq_prefix + parseTableName(entityClass);
		}
		return seqName;
	}

	private ColumnResult parseColumn(Class<? extends Entity> entityClass) {
		List<Field> columnFields = getFieldsByAnnotation(entityClass, Column.class);
		if(CollectionUtils.isEmpty(columnFields)) {
			throw new ParsingException("Annotation " + Column.class.getName() + " not found for " + entityClass.getName());
		}
		ColumnResult columnResult = new ColumnResult();
		//解析实体类属性和数据表字段的映射
		for (Field field  : columnFields) {
			//检查列属性是否符合实体属性规则
			checkColumnFieldType(entityClass, field);
			columnResult.fieldMapping.put(field.getName(), field);
			//获取该字段的get方法名和字段的映射
			try {
				columnResult.getMethodFieldMapping.put(new PropertyDescriptor(field.getName(), entityClass).getWriteMethod().getName(), field);
			} catch (IntrospectionException e) {
				throw new ParsingException(field.getName() + " has no get method in " + entityClass.getName());
			}
			//解析ID字段
			if(field.isAnnotationPresent(Id.class)) {
				if(columnResult.idField == null) {
					if(!Number.class.isAssignableFrom(field.getType())) {
						throw new ParsingException("Id type error for " + entityClass.getName() + ", the type must extends class Number!");
					}
					columnResult.idField = field;
				} else {
					throw new ParsingException("Id field not unique for " + entityClass.getName());
				}
			}
			Column column = field.getAnnotation(Column.class);
			columnResult.fieldAtColumnMapping.put(field, column);
			String name = column.value();
			if(name.equals(Entity.COLUMN_NAME_DEFAULT)) {
				name = getDefaultColumnName(field);
			}
			columnResult.fieldColumnMapping.put(field, name);
			if(columnResult.columnFieldMapping.containsKey(name)) {
				throw new ParsingException("Exist duplicate column " + name + " for " + columnResult.columnFieldMapping.get(name).getName() + " and " + field.getName() + " in " + entityClass.getName() + "!");
			}
			columnResult.columnFieldMapping.put(name, field);
		}
		if(columnResult.idField == null) {
			throw new ParsingException("Annotation " + Id.class.getName() + " not found for " + entityClass.getName());
		}
		return columnResult;
	}

	private void checkColumnFieldType(Class<? extends Entity> entityClass, Field columnField) {
		if(columnField.getGenericType() instanceof ParameterizedType) {
			throw new ParsingException("Column field " + columnField.getName() + " type error for " +
					entityClass.getName() + ", the type can't be generic!");
		}
		Class<?> fieldType = columnField.getType();
		if(fieldType.isPrimitive()) {
			throw new ParsingException("Column field " + columnField.getName() + " type error for " +
					entityClass.getName() + ", the type can't be primitive!");
		}
		if(fieldType.isEnum()) {
			throw new ParsingException("Column field " + columnField.getName() + " type error for " +
					entityClass.getName() + ", the type can't be enum!");
		}
		if(fieldType.isInterface()) {
			throw new ParsingException("Column field " + columnField.getName() + " type error for " +
					entityClass.getName() + ", the type can't be interface!");
		}
	}

	private String getDefaultTableName(Class<? extends Entity> entityClass) {
		return connectEachWordWithUnderline(entityClass.getSimpleName()).toLowerCase();
	}

	private String getDefaultColumnName(Field field) {
		return connectEachWordWithUnderline(field.getName()).toLowerCase();
	}

	private String connectEachWordWithUnderline(String text) {
		if(StringUtils.isEmpty(text)) {
			return text;
		}
		StringBuilder sb = new StringBuilder(String.valueOf(text.charAt(0)));
		for (int i = 1; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c >= 'A' && c <= 'Z') {
				sb.append("_");
			}
			sb.append(c);
		}
		return sb.toString();
	}

	private List<Field> getFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> atClass) {
		List<Field> list = ClassUtils.getInstanceFields(clazz);
		List<Field> ret = new ArrayList<Field>();
		for (Field field : list) {
			if(field.isAnnotationPresent(atClass)) {
				ret.add(field);
			}
		}
		return ret;
	}
	
	private static class ColumnResult {
		private Field idField;
		private Map<String, Field> fieldMapping = new HashMap<String, Field>();
		private Map<Field, Column> fieldAtColumnMapping = new HashMap<Field, Column>();
		private Map<Field, String> fieldColumnMapping = new HashMap<Field, String>();
		private Map<String, Field> columnFieldMapping = new LinkedCaseInsensitiveMap<Field>(); 
		private Map<String, Field> getMethodFieldMapping = new LinkedCaseInsensitiveMap<Field>(); 
		
	}

	public String getSequenceName() {
		return sequenceName;
	}

	public static void setSeq_prefix(String seq_prefix) {
		EntityAnnotationParser.seq_prefix = seq_prefix;
	}

	public Map<String, Field> getGetMethodFieldMapping() {
		return getMethodFieldMapping;
	}

}
