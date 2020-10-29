package com.honlly.mybatis;

public interface SqlComponent {

	/** 空格符 */
	String SPACE = " ";
	/** 参数占位符 */
	String PARAMETER_PLACEHOLDER = "#{?}";
	/** 参数占位符正则表达式 */
	String PARAMETER_PLACEHOLDER_REGEX = "#\\{\\?\\}";
	/** 逗号 */
	String COMMA = ",";
	/** 点号 */
	String DOT = ".";
	/** 星号 */
	String ASTERISK = "*";
	/** 左括号 */
	String LEFT_BRACE = "(";
	/** 右括号 */
	String RIGHT_BRACE = ")";

	/**
	 * 操作符
	 */
	String EQ = "=";
	String LT = "<";
	String GT = ">";
	String NE = "<>";
	String LE = "<=";
	String GE = ">=";
	String LIKE = "LIKE";
	String IS_NULL = "IS NULL";
	String IS_NOT_NULL = "IS NOT NULL";
	String BETWEEN = "BETWEEN";
	String AND = "AND";
	String OR = "OR";
	String IN = "IN";
	String NOT_IN = "NOT IN";
	String UNION_ALL = "UNION ALL";

	/**
	 * SQL增删改查关键字
	 */
	String SELECT = "SELECT";
	String AS = "AS";
	String FROM = "FROM";
	String WHERE = "WHERE";
	String HAVING = "HAVING";
	String GROUP_BY = "GROUP BY";
	String ORDER_BY = "ORDER BY";
	String ASC = "ASC";
	String DESC = "DESC";
	String JOIN = "JOIN";
	String LEFT_JOIN = "LEFT JOIN";
	String RIGHT_JOIN = "RIGHT JOIN";
	String ON = "ON";
	String UPDATE = "UPDATE";
	String SET = "SET";
	String INSERT_INTO = "INSERT INTO";
	String VALUES = "VALUES";
	String DELETE_FROM = "DELETE FROM";
	String IF = "IF";
	String CASE = "CASE";
	String WHEN = "WHEN";
	String THEN = "THEN";
	String ELSE = "ELSE";
	String END = "END";

}
