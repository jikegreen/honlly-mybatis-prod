package com.honlly.entity.domain;

import java.util.List;
import java.util.Map;

import com.honlly.commons.BaseBean;
import com.honlly.commons.SqlComponent;

public class RecordType extends BaseBean implements SqlComponent {
	private static final long serialVersionUID = -1420880042251741446L;
	/** 表名 */
	private String tableName;
	/** 表别名 */
	private String alias;
	/** ID字段名 */
	private String idField;
	/** 表字段集合 */
	private List<String> fields;
	/** 表字段和记录属性映射 */
	private Map<String, RecordField> fieldMapping;

	public RecordType() {
	}

	public RecordType(String tableName) {
		this.tableName = tableName;
	}

	public RecordType(String tableName, String alias) {
		this.tableName = tableName;
		this.alias = alias;
	}

	public RecordType(String tableName, String alias, String idField,
			List<String> fields, Map<String, RecordField> fieldMapping) {
		this.tableName = tableName;
		this.alias = alias;
		this.idField = idField;
		this.fields = fields;
		this.fieldMapping = fieldMapping;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getIdField() {
		return idField;
	}

	public void setIdField(String idField) {
		this.idField = idField;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public Map<String, RecordField> getFieldMapping() {
		return fieldMapping;
	}

	public void setFieldMapping(Map<String, RecordField> fieldMapping) {
		this.fieldMapping = fieldMapping;
	}

}
