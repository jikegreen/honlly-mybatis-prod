/**
 * 
 */
package com.honlly.mybatis.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.honlly.commons.utils.ClassUtils;
import com.honlly.mybatis.Entity;
import com.honlly.mybatis.EntityDao;
import com.honlly.mybatis.IdCol;
import com.honlly.mybatis.dialect.Dialect;

/**
 * 回填id拦截器
 * @date 2015年8月25日 下午5:51:11 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class BackIDInterceptor implements AfterReturningAdvice,MethodBeforeAdvice{
	private Dialect dialect;
	/** sqlSession工厂对象 */
	private SqlSessionFactory sqlSessionFactory;
	private Class<? extends Entity> clazz = null;
	
	@Override
	public void afterReturning(Object result, Method method, Object[] parameter, Object target) throws Throwable {
		if(method.isAnnotationPresent(InsertProvider.class)){
			Long cur = getCurrentSeq();
			if(parameter.length>1){
				@SuppressWarnings("unchecked")
				List<Entity> entities = (List<Entity>) parameter[0];
				Long[] ids = (Long[]) parameter[1];
				if(ids.length>0){
					for (int i = 0; i < ids.length; i++) {
						entities.get(i).setId(ids[i]);
					}
				}else{
					dialect.setMultiEntitiesIds(entities, cur);
				}
			}else{
				if(parameter[0] instanceof Entity){
					Entity entity = (Entity) parameter[0];
					entity.setId(cur);
				}else if(parameter[0] instanceof Map){
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) parameter[0];
					map.put(IdCol.id, cur);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void before(Method method, Object[] parameter, Object target) throws Throwable {
		clazz = (Class<? extends Entity>) target.getClass();
	}
	
	@SuppressWarnings("unchecked")
	private Long getCurrentSeq(){
		SqlSession session = sqlSessionFactory.openSession();
		List<Map<String, Object>> result = session.selectList(dialect.getCurrentSeqSql((Class<? extends Entity>)ClassUtils.getActualTypeArguments(EntityDao.class, clazz)[0]));
		Object currSeq = result.get(0).get("MAXID");
		session.close();
		return dialect.convertSeq(currSeq);
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}
	
}
