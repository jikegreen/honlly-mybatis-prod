package com.honlly.mybatis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
/**
 * dao工厂
 * @date 2015年4月6日 上午10:54:01 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class DaoFactory  {
	/** sqlSession缓存 */
	private static final Map<SqlSessionFactory, SqlSession> sqlSessionCache
			= new ConcurrentHashMap<SqlSessionFactory, SqlSession>();

	public static <D> D getDao(SqlSessionFactory sqlSessionFactory, Class<D> daoClass) {
		return openSession(sqlSessionFactory).getMapper(daoClass);
	}
	
	public static SqlSession getSession(SqlSessionFactory sqlSessionFactory){
		return openSession(sqlSessionFactory);
	}

	private static SqlSession openSession(SqlSessionFactory sqlSessionFactory) {
		SqlSession session = sqlSessionCache.get(sqlSessionFactory);
		if(session != null) {
			return session;
		}
		session = new SqlSessionTemplate(sqlSessionFactory);
		sqlSessionCache.put(sqlSessionFactory, session);
		return session;
	}

}
