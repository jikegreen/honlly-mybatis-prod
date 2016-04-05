package com.honlly.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.type.TypeException;
import org.apache.log4j.Logger;

import com.honlly.commons.domain.Pagination;
import com.honlly.commons.utils.ClassUtils;
import com.honlly.mybatis.dialect.Dialect;
import com.honlly.mybatis.dialect.MySQLDialect;
import com.honlly.mybatis.dialect.OracleDialect;
import com.honlly.mybatis.sqlbuilder.SimpleSqlBuilder;

/**
 * 实际sql方法提供类
 * @date 2015年9月15日 下午8:28:45 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class EntityDaoSqlProvider implements SqlComponent {
	/**
	 * 方法名常量，用于EntityDao中的注解
	 */
	public static final String method_execute = "execute";
	/** @see #getById(Long) */
	public static final String method_getById = "getById";
	/** @see #get(Map) */
	public static final String method_get = "get";
	/** @see #deleteById(Long) */
	public static final String method_deleteById = "deleteById";
	/** @see #delete(Map) */
	public static final String method_delete = "delete";
	/** @see #save(Entity) */
	public static final String method_save = "save";
	/** @see #saveAsMap(Map) */
	public static final String method_saveAsMap = "saveAsMap";
	/** @see #saveOrUpdate(Entity) */
	public static final String method_saveOrUpdate = "saveOrUpdate";
	/** @see #update(Entity) */
	public static final String method_update = "update";
	/** @see #updateAsMap(Map) */
	public static final String method_updateAsMap = "updateAsMap";
	/** @see #query(Map) */
	public static final String method_query = "query";
	/** @see #queryAll(Map) */
	public static final String method_queryAll = "queryAll";
	/** @see #count(Map) */
	public static final String method_count = "count";
	/** @see #deletes(Map) */
	public static final String method_deletes = "deletes";
	/** @see #saves(Map) */
	public static final String method_saves = "saves";
	/** @see #updates(Map) */
	public static final String method_updates = "updates";
	/** @see #saveOrUpdates(Map) */
	public static final String method_saveOrUpdates = "saveOrUpdates";
	
	/** log4j对象 */
	private static final Logger log = Logger.getLogger(EntityDaoSqlProvider.class);
	/** 调用栈最小下标 */
	private static int stackTraceMinIndex;
	/** 调用栈最大下标 */
	private static int stackTraceMaxIndex;
	/** 实体类缓存，key：DAO实现类名称，value：实体类类型  */
	private static final Map<String, Class<? extends Entity>> entityClassCache = new ConcurrentHashMap<String, Class<? extends Entity>>();
	/** 分页SQL语句构建工具类 */
	protected PagingSqlBuilder pagingSqlBuilder;
	
	/** 实体类类型 */
	protected Class<? extends Entity> entityClass;
	/** 实体类注解解析器 */
	protected EntityAnnotationParser entityAnnotationParser;
	/** 数据库方言 **/
	private Dialect dialect;
	/** SQL语句构建对象 */
	protected SimpleSqlBuilder<? extends Entity> simpleSqlBuilder;
	
	public EntityDaoSqlProvider() {
		initStackTraceMinIndex();
		if(entityClass == null) {
			int i = (stackTraceMinIndex + stackTraceMaxIndex) / 2;
			int j = i + 1;
			boolean outOfBounds = false;
			while (i > 0 || !outOfBounds) {
				if(i > 0) {
					try {
						entityClass = parseAndCacheEntityClass(ClassUtils.getStackTraceElement(i).getClassName());
					} catch (Exception e) {
						outOfBounds = true;
					}
					if(entityClass != null) {
						if(i < stackTraceMinIndex) {
							synchronized (EntityDaoSqlProvider.class) {
								if(i < stackTraceMinIndex) {
									stackTraceMinIndex = i;
								}
							}
						}
						break;
					}
					i--;
				}
				if(!outOfBounds) {
					try {
						entityClass = parseAndCacheEntityClass(ClassUtils.getStackTraceElement(j).getClassName());
					} catch(Exception e) {
						outOfBounds = true;
						continue;
					}
					if(entityClass != null) {
						if(j > stackTraceMaxIndex) {
							synchronized (EntityDaoSqlProvider.class) {
								if(j > stackTraceMaxIndex) {
									stackTraceMaxIndex = j;
								}
							}
						}
						break;
					}
					j++;
				}
			}
			if(entityClass == null) {
				throw new TypeException("Not found entity class of dao!");
			}
		}
		if(entityClass == Entity.class) {
			return;
		}
		entityAnnotationParser = EntityAnnotationParser.getInstance(entityClass);
		if(entityAnnotationParser.getSequenceName() == null){
			dialect = new MySQLDialect();
		}else{
			dialect = new OracleDialect();
		}
		simpleSqlBuilder = dialect.getSimpleSqlBuilder(entityClass);
		pagingSqlBuilder = dialect.getPagingSqlBuilder();
	}
	
	/**
	 * 初始化最小栈帧
	 * @Date:2015年9月15日
	 * @author wangk
	 * @Description:
	 */
	private void initStackTraceMinIndex() {
		if(stackTraceMinIndex == 0) {
			synchronized (EntityDaoSqlProvider.class) {
				if(stackTraceMaxIndex == 0) {
					StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
					for (int i = 1; i < stackTraceElements.length; i++) {
						entityClass = parseAndCacheEntityClass(stackTraceElements[i].getClassName());
						if(entityClass == null) {
							continue;
						}
						stackTraceMinIndex = stackTraceMaxIndex = i;
						break;
					}
					if(entityClass == null) {
						throw new TypeException("Not found entity class of dao!");
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Class<? extends Entity> parseAndCacheEntityClass(String className) {
		if(entityClassCache.containsKey(className)) {
			return entityClassCache.get(className);
		}
		Class<?> entityDaoClass = null;
		try {
			entityDaoClass = org.apache.commons.lang3.ClassUtils.getClass(className);
		} catch (ClassNotFoundException e) {
			// ignore ClassNotFoundException
		}
		if(entityDaoClass == null || !EntityDao.class.isAssignableFrom(entityDaoClass)) {
			return null;
		}
		Class<? extends Entity> entityClass = (Class<? extends Entity>)ClassUtils.getActualTypeArguments(EntityDao.class, entityDaoClass)[0];
		entityClassCache.put(className, entityClass);
		return entityClass;
	}
	
	public String execute(String sql) {
		return sql;
	}

	public String getById(Long id) {
		return simpleSqlBuilder.getFindByIdSql();
	}

	public String get(Entity condition) {
		return handleSimpleSql(simpleSqlBuilder.getConditionSql(condition));
	}

	public String deleteById(Long id) {
		return simpleSqlBuilder.getDeleteSql();
	}

	public String delete(Entity condition) {
		String sql = DELETE_FROM + SPACE + entityAnnotationParser.getTableName();
		String conditionSql = simpleSqlBuilder.getConditionSql(condition);
		if(!conditionSql.isEmpty()) {
			sql += SPACE + WHERE + SPACE + conditionSql;
		}
		log.debug(sql);
		return sql;
	}

	public String save(Entity entity) {
		if(!entity.isTransient()) {
			throw new ExecutorException("The record " + entity + " is exist!");
		}
		return simpleSqlBuilder.getInsertSql(simpleSqlBuilder.getSqlFieldSet(entity));
	}

	public String saveAsMap(Map<String, Object> fieldParamMapping) {
		if(fieldParamMapping == null) {
			fieldParamMapping = new HashMap<String, Object>();
		}
		String sql = simpleSqlBuilder.getIncludeIdFieldInsertSql(fieldParamMapping.keySet());
		log.debug(sql);
		return sql;
	}

	public String saveOrUpdate(Entity entity) {
		if(entity.isTransient()) {
			return save(entity);
		}
		return update(entity);
	}

	public String update(Entity entity) {
		if(entity.isTransient()) {
			throw new ExecutorException("The record " + entity + " is transient!");
		}
		return simpleSqlBuilder.getUpdateSql(simpleSqlBuilder.getSqlFieldSet(entity));
	}

	public String updateAsMap(Map<String, Object> args) {
		Entity condition = (Entity)args.get("param1");
		@SuppressWarnings("unchecked")
		Map<String, Object> fieldParamMapping = (Map<String, Object>)args.get("param2");
		if(fieldParamMapping == null || fieldParamMapping.isEmpty()) {
			return null;
		}
		int updateFieldCount = 0;
		for (String field : fieldParamMapping.keySet()) {
			if(entityAnnotationParser.getFieldMapping().containsKey(field) && !field.equals(entityAnnotationParser.getIdField().getName())) {
				updateFieldCount++;
			}
		}
		if(updateFieldCount == 0) {
			return null;
		}
		StringBuilder sql = simpleSqlBuilder.getUpdateAsMapSql(fieldParamMapping);
		String conditionSql = simpleSqlBuilder.getConditionSql(condition, "param1");
		if(!conditionSql.isEmpty()) {
			sql.append(SPACE + WHERE + SPACE).append(conditionSql);
		}
		log.debug(sql);
		return sql.toString();
	}

	public String query(Map<String, Object> args) {
		Entity condition = (Entity)args.get("param1");
		Pagination pagination = (Pagination)args.get("param2");
		String sql = simpleSqlBuilder.getConditionSql(condition, "param1");
		if(!sql.isEmpty()) {
			sql += SPACE;
		}
		sql += ORDER_BY + SPACE + entityAnnotationParser.getFieldColumnMapping().get(entityAnnotationParser.getIdField());
		sql = handleSimpleSql(sql);
		if(pagination != null && !pagination.isInvalid()) {
			sql = pagingSqlBuilder.getPagingSql(sql, pagination);
		}
		log.debug(sql);
		return sql;
	}

	public String queryAll(Pagination pagination) {
		String sql = simpleSqlBuilder.getQueryAllSql() + SPACE + ORDER_BY + SPACE +
				entityAnnotationParser.getFieldColumnMapping().get(entityAnnotationParser.getIdField());
		if(pagination != null && !pagination.isInvalid()) {
			sql = pagingSqlBuilder.getPagingSql(sql, pagination);
		}
		return sql;
	}

	public String count(Entity condition) {
		String sql = simpleSqlBuilder.getQueryCountSql();
		String conditionSql = simpleSqlBuilder.getConditionSql(condition);
		if(!conditionSql.isEmpty()) {
			sql += SPACE + WHERE + SPACE + conditionSql;
		}
		log.debug(sql);
		return sql;
	}

	public String deletes(Map<String, Object> args) {
		@SuppressWarnings("unchecked")
		List<Long> ids = (List<Long>)args.get("param1");
		if(CollectionUtils.isEmpty(ids)) {
			return null;
		}
		StringBuilder sql = new StringBuilder(DELETE_FROM + SPACE + entityAnnotationParser.getTableName() + SPACE + WHERE + SPACE
				+ entityAnnotationParser.getFieldColumnMapping().get(entityAnnotationParser.getIdField()) + SPACE + IN + SPACE + LEFT_BRACE);
		for (int i = 0; i < ids.size(); i++) {
			sql.append("#{param1[" + i + "]}").append(COMMA + SPACE);
		}
		sql.delete(sql.length()-2, sql.length());
		sql.append(RIGHT_BRACE);
		log.debug(sql);
		return sql.toString();
	}

	public String saves(Map<String, Object> args) {
		@SuppressWarnings("unchecked")
		List<Entity> entitys = (List<Entity>)args.get("param1");
		if(CollectionUtils.isEmpty(entitys)) {
			return null;
		}
		Long[] ids = null;
		try {
			ids = (Long[])args.get("param2");
		} catch (Exception e) {
			// ignore e
		}
		if(ids == null) {
			ids = new Long[0];
		}
		String sql = simpleSqlBuilder.getBatchInsertSql(entitys, ids);
		log.debug(sql);
		return sql;
	}

	public String updates(Map<String, Object> args) {
		@SuppressWarnings("unchecked")
		List<Entity> entitys = (List<Entity>)args.get("param1");
		String sql = simpleSqlBuilder.getUpdatesSql(entitys);
		log.debug(sql);
		return sql;
	}

	public String saveOrUpdates(Map<String, Object> args) {
		@SuppressWarnings("unchecked")
		List<Entity> entitys = (List<Entity>)args.get("param1");
		if(CollectionUtils.isEmpty(entitys)) {
			return null;
		}
		if(entitys.get(0).isTransient()) {
			return saves(args);
		}
		return updates(args);
	}

	protected String handleSimpleSql(String sql) {
		if(StringUtils.isBlank(sql)) {
			sql = simpleSqlBuilder.getQueryAllSql();
		} else if(!sql.trim().toUpperCase().startsWith("SELECT")) {
			if(!sql.trim().toUpperCase().startsWith("WHERE") && !sql.trim().toUpperCase().startsWith("ORDER")) {
				sql = WHERE + SPACE + sql;
			}
			sql = simpleSqlBuilder.getQueryAllSql() + " " + sql;
		}
		log.debug(sql);
		return sql;
	}

	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}

}
