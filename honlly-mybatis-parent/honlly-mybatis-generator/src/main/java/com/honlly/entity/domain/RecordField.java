package com.honlly.entity.domain;

import java.io.Serializable;

import com.honlly.commons.BaseBean;
import com.honlly.commons.SqlComponent;
import com.honlly.dialect.Dialect;

public class RecordField extends BaseBean implements SqlComponent {
	private static final long serialVersionUID = -616437214773100627L;
	/** 表前缀 */
	private String tablePrefix;
	/** 列名 */
	private String columnName;
	/** 列别名 */
	private String alias;
	/** 数据库方言，用于映射数据库类型 */
	private Dialect dialect;
	/** 字段类型，取值为java.sql.Types中的常量值  */
	private int type;
	/** 字段长度，默认值0表示未指定长度  */
	public int size;
	/** 是否有唯一性约束标志，默认false表示没有唯一性约束  */
	public boolean unique;
	/** 是否不允许为空 */
	private boolean notNull;
	/** 默认值，默认null表示没有默认值  */
	private Serializable defaultValue;

	public RecordField() {
	}
	
	public RecordField(String columnName) {
		this.columnName = columnName;
	}

	public RecordField(String tablePrefix, String columnName) {
		this.tablePrefix = tablePrefix;
		this.columnName = columnName;
	}
	
	public RecordField(String tablePrefix, String columnName, String alias) {
		this.tablePrefix = tablePrefix;
		this.columnName = columnName;
		this.alias = alias;
	}

	public RecordField(String tablePrefix, String columnName, String alias,
			Dialect dialect, int type, int size, boolean unique,
			boolean notNull, Serializable defaultValue) {
		this.tablePrefix = tablePrefix;
		this.columnName = columnName;
		this.alias = alias;
		this.dialect = dialect;
		this.type = type;
		this.size = size;
		this.unique = unique;
		this.notNull = notNull;
		this.defaultValue = defaultValue;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public Dialect getDialect() {
		return dialect;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public boolean isNotNull() {
		return notNull;
	}

	public void setNotNull(boolean notNull) {
		this.notNull = notNull;
	}

	public Serializable getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Serializable defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/*
	 * build column method
	 */
	public RecordField col(String columnName) {
		setColumnName(columnName);
		return this;
	}

	public RecordField as(String alias) {
		setAlias(alias);
		return this;
	}

	public static RecordField table(String tablePrefix) {
		RecordField field = new RecordField();
		field.setTablePrefix(tablePrefix);
		return field;
	}

	public static RecordField column(String columnName) {
		return new RecordField(columnName);
	}

}
