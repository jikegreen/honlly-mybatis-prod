package com.honlly.mybatis;

public interface SqlComponent {

	/** 空格符 */
	public static final String SPACE = " ";
	/** 参数占位符 */
	public static final String PARAMETER_PLACEHOLDER = "#{?}";
	/** 参数占位符正则表达式 */
	public static final String PARAMETER_PLACEHOLDER_REGEX = "#\\{\\?\\}";
	/** 逗号 */
	public static final String COMMA = ",";
	/** 点号 */
	public static final String DOT = ".";
	/** 星号 */
	public static final String ASTERISK = "*";
	/** 左括号 */
	public static final String LEFT_BRACE = "(";
	/** 右括号 */
	public static final String RIGHT_BRACE = ")";

	/**
	 * 操作符
	 */
	public static final String EQ = "=";
	public static final String LT = "<";
	public static final String GT = ">";
	public static final String NE = "<>";
	public static final String LE = "<=";
	public static final String GE = ">=";
	public static final String LIKE = "LIKE";
	public static final String IS_NULL = "IS NULL";
	public static final String IS_NOT_NULL = "IS NOT NULL";
	public static final String BETWEEN = "BETWEEN";
	public static final String AND = "AND";
	public static final String OR = "OR";
	public static final String IN = "IN";
	public static final String NOT_IN = "NOT IN";
	public static final String UNION_ALL = "UNION ALL";

	/**
	 * SQL增删改查关键字
	 */
	public static final String SELECT = "SELECT";
	public static final String AS = "AS";
	public static final String FROM = "FROM";
	public static final String WHERE = "WHERE";
	public static final String HAVING = "HAVING";
	public static final String GROUP_BY = "GROUP BY";
	public static final String ORDER_BY = "ORDER BY";
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	public static final String JOIN = "JOIN";
	public static final String LEFT_JOIN = "LEFT JOIN";
	public static final String RIGHT_JOIN = "RIGHT JOIN";
	public static final String ON = "ON";
	public static final String UPDATE = "UPDATE";
	public static final String SET = "SET";
	public static final String INSERT_INTO = "INSERT INTO";
	public static final String VALUES = "VALUES";
	public static final String DELETE_FROM = "DELETE FROM";
	public static final String IF = "IF";
	public static final String CASE = "CASE";
	public static final String WHEN = "WHEN";
	public static final String THEN = "THEN";
	public static final String ELSE = "ELSE";
	public static final String END = "END";

}
