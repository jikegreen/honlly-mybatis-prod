package com.honlly.factory;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DaoFactory  {
	private DaoFactory(){}
	/** sqlSession缓存 */
	private static final Map<SqlSessionFactory, SqlSession> sqlSessionCache
			= new ConcurrentHashMap<>();

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
