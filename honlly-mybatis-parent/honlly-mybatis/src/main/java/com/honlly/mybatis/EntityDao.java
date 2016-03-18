package com.honlly.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.honlly.commons.domain.Pagination;

/**
 * 查询接口
 * @date 2015年9月15日 下午8:31:31 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public interface EntityDao<E extends Entity> {
	/**  实体类结果映射ID，必须在DAO对应的mapper配置文件中定义 */
	String ENTITY_RESULT_MAP_ID = "entityResultMap";
	/** 第一个参数名称 */
	String FIRST_PARAM_NAME = "param1";
	
	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_execute)
	@ResultType(Map.class)
	public List<Map<String, Object>> execute(String sql);

	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_getById)
	@ResultMap(ENTITY_RESULT_MAP_ID)
	public E getById(Long id);

	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_get)
	@ResultMap(ENTITY_RESULT_MAP_ID)
	public E get(E condition);

	@DeleteProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_deleteById)
	public void deleteById(Long id);

	@DeleteProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_delete)
	public void delete(E condition);

	@InsertProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_save)
	public void save(E entity);

	@InsertProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_saveAsMap)
	public void saveAsMap(Map<String, Object> fieldParamMapping);

	@InsertProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_saveOrUpdate)
	public void saveOrUpdate(E entity);

	@UpdateProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_update)
	public void update(E entity);

	@UpdateProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_updateAsMap)
	public void updateAsMap(E condition, Map<String, Object> fieldParamMapping);
	
	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_query)
	@ResultMap(ENTITY_RESULT_MAP_ID)
	public List<E> query(E condition, Pagination pagination);

	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_queryAll)
	@ResultMap(ENTITY_RESULT_MAP_ID)
	public List<E> queryAll(Pagination pagination);

	@SelectProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_count)
	public int count(E condition);
	
	@DeleteProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_deletes)
	public void deletes(@Param(FIRST_PARAM_NAME)List<Long> ids);

	@InsertProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_saves)
	public void saves(List<E> entitys, Long... ids);

	@UpdateProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_updates)
	public void updates(@Param(FIRST_PARAM_NAME)List<E> entitys);

	@UpdateProvider(type=EntityDaoSqlProvider.class,method=EntityDaoSqlProvider.method_saveOrUpdates)
	public void saveOrUpdates(@Param(FIRST_PARAM_NAME)List<E> entitys);

}
