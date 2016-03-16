package com.honlly.entity.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.honlly.dialect.Dialect;
import com.honlly.entity.dao.RecordTypeDao;
import com.honlly.entity.domain.RecordField;
import com.honlly.entity.domain.RecordType;
import com.honlly.factory.DaoFactory;

public class RecordTypeProvider {
	
	private Dialect dialect;
	/** 表名集合 */
	private final List<String> talbeNames = new ArrayList<String>();
	/** 记录类型缓存 */
	private final Map<String, RecordType> recordTypeCache = new LinkedCaseInsensitiveMap<RecordType>();
	/** sqlSession工厂对象 */
	private SqlSessionFactory sqlSessionFactory;

	public RecordTypeProvider() {
	}

	public RecordTypeProvider(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public List<String> getTableNames() {
		initRecordTypeCache();
		return talbeNames;
	}
	
	public RecordType getRecordType(String tableName) {
		initRecordTypeCache();
		return recordTypeCache.get(tableName);
	}
	
	private void initRecordTypeCache() {
		if(recordTypeCache.isEmpty()) {
			synchronized (this) {
				if(recordTypeCache.isEmpty()) {
					RecordTypeDao dao = DaoFactory.getDao(sqlSessionFactory, RecordTypeDao.class);
					List<Map<String, Object>> result = dao.select(dialect.toSqlString());
					for (Map<String, Object> map : result) {
						String tableName = map.get(Dialect.META_TABLE_NAME).toString().toLowerCase();
						if(!talbeNames.contains(tableName)) {
							talbeNames.add(tableName);
							recordTypeCache.put(tableName, new RecordType(tableName, tableName, null, new ArrayList<String>(), new LinkedCaseInsensitiveMap<RecordField>()));
						}
						RecordType recordType = recordTypeCache.get(tableName);
						String columnName = map.get(Dialect.META_COLUMN_NAME).toString().toLowerCase();
						RecordField field = new RecordField(tableName, columnName, (String)map.get(Dialect.META_COLUMN_COMMENT));
						field.setDialect(dialect);
						DbFieldUtils.setFieldType(field, map.get(Dialect.META_COLUMN_TYPE).toString().toUpperCase());
						Object primaryKey = map.get(Dialect.META_COLUMN_KEY);
						if(primaryKey instanceof String){
							String columnKey = (String)primaryKey;
							if(columnKey != null) {
								if(columnKey.toUpperCase().indexOf(Dialect.META_VALUE_COLUMN_KEY_PRIMARY.toUpperCase()) >= 0) {
									recordType.setIdField(columnName);
								}
								if(columnKey.toUpperCase().indexOf(Dialect.META_VALUE_COLUMN_KEY_UNIQUE.toUpperCase()) >= 0) {
									field.setUnique(true);
								}
							}
						}else if(primaryKey instanceof BigDecimal){
							BigDecimal columnKey = (BigDecimal)primaryKey;
							if(columnKey != null) {
								if(columnKey.toBigInteger().intValue() == 1){
									recordType.setIdField(columnName);
									field.setUnique(true);
								}
							}
						}
						
						Serializable defaultValue = (Serializable)map.get(Dialect.META_COLUMN_DEFAULT);
						if(defaultValue != null) {
							field.setDefaultValue(defaultValue);
						}
						String nullable = (String)map.get(Dialect.META_COLUMN_NULLABLE);
						if(!Dialect.META_VALUE_COLUMN_NULLABLE.equalsIgnoreCase(nullable) && 
								!Dialect.META_VALUE_COLUMN_NULLABLE_YES.equalsIgnoreCase(nullable) &&
								!Dialect.META_VALUE_COLUMN_NULLABLE_TRUE.equalsIgnoreCase(nullable) &&
								!Dialect.META_VALUE_COLUMN_NULLABLE_ONE.equals(nullable)) {
							field.setNotNull(true);
						}
						recordType.getFields().add(columnName);
						recordType.getFieldMapping().put(columnName, field);
					}
				}
			}
		}
	}

	public synchronized void clearRecordTypeCache() {
		talbeNames.clear();
		recordTypeCache.clear();
	}

	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

}
