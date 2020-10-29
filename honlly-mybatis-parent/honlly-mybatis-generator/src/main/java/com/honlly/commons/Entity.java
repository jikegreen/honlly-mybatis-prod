package com.honlly.commons;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.honlly.annotation.Column;
import com.honlly.annotation.Id;
import com.honlly.parser.EntityAnnotationParser;

/**
 * 基础实体
 * @date 2015年9月15日 下午8:31:13 
 * @author wangk
 * @Description:
 * @project honlly-mybatis
 */
public class Entity extends BaseBean implements Comparable<Entity> {
	/** 序列化版本标识 */
	private static final long serialVersionUID = 4224390531787078169L;

	/** 注解表名默认值 */
	public static final String TABLE_NAME_DEFAULT = "";
	/** 序列名默认值 */
	public static final String SEQ_NAME_DEFAULT = "";
	/** 注解列名默认值 */
	public static final String COLUMN_NAME_DEFAULT = "";

	/** 表ID */
	@Id
	@Column(IdCol.ID)
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? super.hashCode() : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
			return super.equals(obj);
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public int compareTo(Entity other) {
		if(isTransient() && other.isTransient()) {
			int cmp = hashCode() - other.hashCode();
			return cmp == 0 ? 0 : cmp > 0 ? 1 : -1;
		}
		if(isTransient()) {
			return -1;
		}
		if(other.isTransient()) {
			return 1;
		}
		return id.compareTo(other.getId());
	}

	public List<String> compareColumnFields(Entity other) {
		if(!getClass().isInstance(other)) {
			return null;
		}
		List<String> differentColumnFields = new ArrayList<>();
		for (Field field : EntityAnnotationParser.getInstance(getClass()).getFieldMapping().values()) {
			Object value = null;
			Object otherValue = null;
			try {
				Method method = new PropertyDescriptor(field.getName(), getClass()).getReadMethod();
				value = method.invoke(this);
				otherValue = method.invoke(other);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if(value==null && otherValue!=null || value!=null && !value.equals(otherValue)) {
				differentColumnFields.add(field.getName());
			}
		}
		return differentColumnFields;
	}

	public <T> T get(String column) {
		EntityAnnotationParser annotationParser = EntityAnnotationParser.getInstance(getClass());
		Field field = null;
		if(annotationParser.getColumnFieldMapping().containsKey(column)) {
			field = annotationParser.getColumnFieldMapping().get(column);
		} else {
			field = annotationParser.getFieldMapping().get(column);
		}
		if(field == null) {
			return null;
		}
		try {
			@SuppressWarnings("unchecked")
			T value = (T)new PropertyDescriptor(field.getName(), getClass()).getReadMethod().invoke(this);
			return value;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isTransient() {
		return id == null;
	}

}
