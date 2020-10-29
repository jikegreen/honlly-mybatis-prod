package com.honlly;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.honlly.commons.IdCol;
import com.honlly.dialect.MySQLDialect;
import com.honlly.dialect.OracleDialect;
import com.honlly.entity.domain.Config;
import com.honlly.entity.domain.RecordField;
import com.honlly.entity.utils.DbFieldUtils;
import com.honlly.entity.utils.FileUtils;
import com.honlly.entity.utils.RecordTypeProvider;

@Component
public class EntityCreater implements InitializingBean {

	public static final String ENTER = "\n";
	public static final String TAB = "    ";
	public static final String NOW_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	public static final Map<Integer, String> TYPE_MAPPING;
	static {
		Map<Integer, String> map = new HashMap<>();
		for (Field field : Types.class.getFields()) {
			if(!Modifier.isStatic(field.getModifiers()) || field.getType() != int.class) {
				continue;
			}
			try {
				map.put((Integer)field.get(null), field.getName());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		TYPE_MAPPING = Collections.unmodifiableMap(map);
	}

	public static final String DEFAULT_TYPE = "mybatis";

	private String type = DEFAULT_TYPE;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Autowired
	protected RecordTypeProvider provider;
	@Autowired
	protected Config config;
	private static final List<String> IGNORE_TABLE_PREFIX = new ArrayList<>();
	/** 默认为mysql **/
	private String dialect = "mysql";
	

	@Override
	public void afterPropertiesSet() throws Exception {
		String ignoreTablePrefix = config.getIgnoreTablePrefix();
		if(ignoreTablePrefix.length() > 0) {
			String[] ignoreTablePrefixs = ignoreTablePrefix.split("\\s*\\,\\s*");
			for (String elem : ignoreTablePrefixs) {
				IGNORE_TABLE_PREFIX.add(elem);
			}
		}
		dialect = provider.getSqlSessionFactory().getConfiguration().getEnvironment().getDataSource().getConnection().getMetaData().getDatabaseProductName();
		if("oracle".equalsIgnoreCase(dialect)){
			OracleDialect oracleDialect = new OracleDialect();
			oracleDialect.setUsers(config.getUser().split(","));
			provider.setDialect(oracleDialect);
		}else if("mysql".equalsIgnoreCase(dialect)){
			provider.setDialect(new MySQLDialect());
		}else{
			throw new RuntimeException("数据库:"+dialect+"，暂不支持！！！");
		}
	}

	public void create() throws Exception {
		List<String> tables = null;
		List<String> allTables = provider.getTableNames();
		List<String> needTables = Arrays.asList(config.getTableNames().split(","));
		if(needTables.size() == 0){
			tables = allTables;
		}else{
			tables = needTables;
		}
		for (String table : tables) {
			createEntityClass(table);
			createDaoClass(table);
			createDaoMapper(table);
		}
	}

	public void createEntityClass(String table) throws Exception {
		String tableConstantName = getTableConstantName(table);
		String className = getClassName(tableConstantName);
		StringBuilder sb = new StringBuilder();
		sb.append("package " + config.getRootPackage() + ".domain.entity." + type + ";");
		sb.append(ENTER);
		sb.append(ENTER);
		sb.append("/**\n * \n *\n * @created：" + NOW_DATE + "\n * @author " + config.getAuthor() + "\n */");
		sb.append(ENTER);
		sb.append("@Table");
		if(!tableConstantName.equalsIgnoreCase(table)) {
			sb.append("(\"" + table + "\")");
		}
		sb.append(ENTER);
		if("oracle".equalsIgnoreCase(dialect)){
			sb.append("@Sequence");
			sb.append("(\"SEQ_" + table + "\")");
			sb.append(ENTER);
		}
		sb.append("public class " + className + " extends Entity {");
		sb.append(ENTER);
		sb.append(ENTER);
		
		sb.append(TAB);
		sb.append("/**");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append(" * 实体元数据(列属性常量接口)");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append(" */");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append("public interface " + className + "Meta extends IdCol {");
		sb.append(ENTER);
		for (String field : provider.getRecordType(table).getFields()) {
			if(field.equalsIgnoreCase(provider.getRecordType(table).getIdField())) {
				continue;
			}
			sb.append(TAB);
			sb.append(TAB);
			sb.append("String " + getFieldName(field) + " = \"" + getFieldName(field) + "\";");
			sb.append(ENTER);
		}
		sb.append(TAB);
		sb.append("}");
		sb.append(ENTER);
		sb.append(ENTER);
		sb.append(TAB);
		sb.append("/**");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append(" * 列属性");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append(" */");
		sb.append(ENTER);
		for (String field : provider.getRecordType(table).getFields()) {
			RecordField col = provider.getRecordType(table).getFieldMapping().get(field);
			if(field.equalsIgnoreCase(provider.getRecordType(table).getIdField())) {
				continue;
			}
			sb.append(TAB);
			String alias = "";
			Class<? extends Serializable> fieldType = DbFieldUtils.getFieldClass(col.getType());
			if(fieldType == null) {
				alias += "TODO check type: " + col.getType() + "; ";
				fieldType = String.class;
			}
			if(col.getAlias() != null) {
				alias += col.getAlias();
			}
			sb.append("/** " + alias + " */");
			sb.append(ENTER);
			sb.append(TAB);
			sb.append("@Column");
			StringBuilder atCol = new StringBuilder();
			/*if(col.getType() != 0) {
 				atCol.append("type=Types.").append(TYPE_MAPPING.get(col.getType())).append(",");
			}
			if(col.getSize() > 0) {
 				atCol.append("size=").append(col.getSize()).append(",");
			}
			if(col.isUnique()) {
 				atCol.append("isUnique=true,");
			}*/
			if(col.isNotNull()) {
 				atCol.append("isNotNull=true,");
			}
			/*if(col.getDefaultValue() != null) {
 				atCol.append("hasDefault=true,");
			}*/
			if(atCol.length() > 0) {
				sb.append("("+atCol.deleteCharAt(atCol.length()-1)+")");
			}
			sb.append(ENTER);
			sb.append(TAB);
			sb.append("private ");
			if(Number.class.isAssignableFrom(fieldType) && (field.equalsIgnoreCase("ID") || field.toLowerCase().endsWith("_id"))) {
				sb.append("Long");
			} else {
				sb.append(fieldType.getSimpleName());
			}
			sb.append(" " + getFieldName(field) + ";");
			sb.append(ENTER);
		}
		sb.append(ENTER);
		sb.append("}");
		sb.append(ENTER);
		FileUtils.save("src/main/java/" + config.getRootPackage().replaceAll("\\.", "/") + "/domain/entity/" + type + "/" + className + ".java", sb.toString());
	}

	public void createDaoClass(String table) throws Exception {
		String className = getClassName(getTableConstantName(table));
		StringBuilder sb = new StringBuilder();
		sb.append("package " + config.getRootPackage() + ".dao." + type + ";");
		sb.append(ENTER);
		sb.append(ENTER);
		sb.append("/**\n * \n *\n * @created：" + NOW_DATE + "\n * @author " + config.getAuthor() + "\n */");
		sb.append(ENTER);
		sb.append("public interface " + className + "Dao extends EntityDao<" + className + "> {");
		sb.append(ENTER);
		sb.append(ENTER);
		sb.append("}");
		sb.append(ENTER);
		FileUtils.save("src/main/java/" + config.getRootPackage().replaceAll("\\.", "/") + "/dao/" + type + "/" + className + "Dao.java", sb.toString());
	}

	public void createDaoMapper(String table) {
		String className = getClassName(getTableConstantName(table));
		StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append(ENTER);
		sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		sb.append(ENTER);
		sb.append("<mapper namespace=\"" + config.getRootPackage() + ".dao." + type + "." + className + "Dao\">");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append("<resultMap ID=\"entityResultMap\" type=\"" + className + "\">");
		sb.append(ENTER);
		sb.append(TAB);
		sb.append(TAB);
		sb.append("<ID property=\"ID\" column=\"" + IdCol.ID + "\" />");
		for (String field : provider.getRecordType(table).getFields()) {
			if(field.equalsIgnoreCase(provider.getRecordType(table).getIdField())) {
				continue;
			}
			sb.append(ENTER);
			sb.append(TAB);
			sb.append(TAB);	
			sb.append("<result property=\"" + getFieldName(field) + "\" column=\"" + field + "\" />");
		}
		sb.append(ENTER);
		sb.append(TAB);
		sb.append("</resultMap>");
		sb.append(ENTER);
		sb.append(ENTER);
		sb.append("</mapper>");
		sb.append(ENTER);
		FileUtils.save("src/main/resources/" + config.getRootPackage().replaceAll("\\.", "/") + "/dao/" + type + "/" + className + "Dao.xml", sb.toString());
	}

	protected String getTableConstantName(String table) {
		String tableConstantName = table.toUpperCase();
		for (String item : IGNORE_TABLE_PREFIX) {
			tableConstantName = tableConstantName.replaceAll("^" + item.toUpperCase(), "");
		}
		return tableConstantName;
	}

	protected String getClassName(String name) {
		String[] names = name.split("_");
		StringBuilder sb = new StringBuilder();
		for (String n : names) {
			if(n.length() == 0) {
				sb.append("_");
			} else {
				sb.append(n.substring(0, 1).toUpperCase());
				if(n.length() > 1) {
					sb.append(n.substring(1).toLowerCase());
				}
			}
		}
		return sb.toString();
	}

	protected String getFieldName(String name) {
		String _name = getClassName(name);
		return _name.substring(0, 1).toLowerCase() + _name.substring(1);
	}

}
