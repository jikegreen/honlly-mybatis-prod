/**
 * 
 */
package com.honlly.mybatis.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import com.honlly.commons.utils.ClassUtils;
import com.honlly.mybatis.Entity;
import com.honlly.mybatis.EntityDao;
import com.honlly.mybatis.IdCol;
import com.honlly.mybatis.annotation.Sequence;
import com.honlly.mybatis.dialect.Dialect;
import com.honlly.mybatis.dialect.MySQLDialect;
import com.honlly.mybatis.dialect.OracleDialect;

/**
 * 回填id拦截器
 * @date 2015年8月25日 下午5:51:11 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class BackIDInterceptor implements AfterReturningAdvice,MethodBeforeAdvice{
	private static final Logger LOGGER = Logger.getLogger(BackIDInterceptor.class); 
	private Class<? extends Entity> clazz = null;
	
	@Override
	public void afterReturning(Object result, Method method, Object[] parameter, Object target) throws Throwable {
		if(!EntityDao.class.isInstance(target)){
			LOGGER.error("被切入对象不是EntityDao的子类或它本身，请检查！！！");
			return;
		}
		EntityDao<?> dao = (EntityDao<?>) target;
		String dialectName = ClassUtils.getActualTypeArguments(EntityDao.class, clazz)[0].isAnnotationPresent(Sequence.class)? "oracle":"mysql";
		Dialect dialect = new MySQLDialect(); 
		if("oracle".equalsIgnoreCase(dialectName)){
			dialect = new OracleDialect();
		}else if("mysql".equalsIgnoreCase(dialectName)){
			
		}else{
			throw new RuntimeException("数据库:"+dialect+"，暂不支持！！！");
		}
		if(method.isAnnotationPresent(InsertProvider.class)){
			Long cur = getCurrentSeq(dao,dialect);
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
	private Long getCurrentSeq(EntityDao<?> dao, Dialect dialect){
		List<Map<String, Object>> result = dao.execute(dialect.getCurrentSeqSql((Class<? extends Entity>)ClassUtils.getActualTypeArguments(EntityDao.class, clazz)[0]));
		Object currSeq = result.get(0).get("MAXID");
		return dialect.convertSeq(currSeq);
	}

}
