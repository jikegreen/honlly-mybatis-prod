package com.honlly.entity.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface RecordTypeDao {

	List<Map<String, Object>> select(@Param("sql") String sql);

}
