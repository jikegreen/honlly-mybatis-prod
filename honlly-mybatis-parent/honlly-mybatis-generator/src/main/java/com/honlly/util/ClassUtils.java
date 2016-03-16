package com.honlly.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassUtils {

	/** 继承类型实际类型参数缓存，key: 原始类型，value.key: 继承类型 */
	private static final Map<Class<?>, Map<Class<?>, Class<?>[]>> extendActualTypeArgumentsCache = new ConcurrentHashMap<Class<?>, Map<Class<?>, Class<?>[]>>();
	/** 获取方法调用栈元素的方法反射对象 */
	private static final Method getStackTraceElementMethod;
	/**
	 * 初始化getStackTraceElementMethod并将其设为可访问的
	 */
	static {
		try {
			getStackTraceElementMethod = Throwable.class.getDeclaredMethod("getStackTraceElement", int.class);
			getStackTraceElementMethod.setAccessible(true);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取方法调用栈
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param stackTraceIndex 栈帧
	 * @return
	 * @Description:
	 */
	public static StackTraceElement getStackTraceElement(int stackTraceIndex) {
		try {
			return (StackTraceElement)getStackTraceElementMethod.invoke(new Throwable(), stackTraceIndex);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 递归获取类成员
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param clazz 当前类对象
	 * @return
	 * @Description:
	 */
	public static List<Field> getInstanceFields(Class<?> clazz) {
		List<Field> list = new ArrayList<Field>();
		for (Field field : clazz.getDeclaredFields()) {
			if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
				list.add(field);
			}
		}
		Class<?> superClass = clazz.getSuperclass();
		if(superClass != null) {
			list.addAll(getInstanceFields(superClass));
		}
		return list;
	}
	
	/**
	 * 获取原始类型
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param type 类型接口
	 * @return
	 * @Description:
	 */
	public static Class<?> getRawClass(Type type) {
		if(type instanceof Class) {
			return (Class<?>)type;
		}
		if(type instanceof ParameterizedType) {
			return getRawClass(((ParameterizedType)type).getRawType());
		}
		if(type instanceof TypeVariable) {
			return getRawClass(((TypeVariable<?>)type).getBounds()[0]);
		}
		if(type instanceof WildcardType) {
			WildcardType wildcard = (WildcardType)type;
			Type[] lowerBounds = wildcard.getLowerBounds();
			return getRawClass(lowerBounds.length == 0 ? wildcard.getUpperBounds()[0] : lowerBounds[0]);
		}
		if(type instanceof GenericArrayType) {
			return Array.newInstance(getRawClass(((GenericArrayType)type).getGenericComponentType()), 0).getClass();
		}
		throw new RuntimeException("Unknown type for " + type + "!");
	}
	
	/**
	 * 获取泛型的实际类型
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param rawType 父类型
	 * @param extendType 子类型
	 * @return
	 * @Description:
	 */
	public static Class<?>[] getActualTypeArguments(Class<?> rawType, Class<?> extendType) {
		if(!rawType.isAssignableFrom(extendType)) {
			return null;
		}
		TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
		if(typeParameters.length == 0) {
			return new Class<?>[0];
		}
		Class<?>[] actualTypeArguments = null;
		Map<Class<?>, Class<?>[]> actualTypeArgumentsCache = extendActualTypeArgumentsCache.get(rawType);
		if(actualTypeArgumentsCache != null) {
			actualTypeArguments = actualTypeArgumentsCache.get(extendType);
		} else {
			actualTypeArgumentsCache = new HashMap<Class<?>, Class<?>[]>();
			extendActualTypeArgumentsCache.put(rawType, actualTypeArgumentsCache);
		}
		if(actualTypeArguments != null) {
			return actualTypeArguments;
		}
		actualTypeArguments = new Class<?>[typeParameters.length];
		List<Type> extendLink = new ArrayList<Type>();
		Type itemType = extendType;
		do {
			extendLink.add(itemType);
		} while ((itemType = checkTypeDirectCompatible(rawType, itemType)) != null);
		extendLink.addAll(getDirectCompatibleExtendLink(rawType, extendLink.remove(extendLink.size()-1)));
		//key: extendType的参数下标，rawType的参数下标
		Map<Integer, Integer> indexMapping = new HashMap<Integer, Integer>();
		for (int i = 0; i < actualTypeArguments.length; i++) {
			indexMapping.put(i, i);
		}
		for (int i = extendLink.size()-1; i > 0; i--) {
			if(indexMapping.isEmpty() || !(extendLink.get(i) instanceof ParameterizedType)) {
				break;
			}
			Type[] params = ((ParameterizedType)extendLink.get(i)).getActualTypeArguments();
			Map<Integer, Integer> nextIndexMapping = new HashMap<Integer, Integer>();
			for (Integer j : indexMapping.keySet()) {
				Type param = params[j];
				actualTypeArguments[indexMapping.get(j)] = getRawClass(param);
				if((param instanceof TypeVariable) && (extendLink.get(i-1) instanceof ParameterizedType)) {
					TypeVariable<?>[] vars = getRawClass(extendLink.get(i-1)).getTypeParameters();
					for (int k = 0; k < vars.length; k++) {
						if(vars[k].equals(param)) {
							nextIndexMapping.put(k, indexMapping.get(j));
							break;
						}
					}
				}
			}
			indexMapping = nextIndexMapping;
		}
		for (int i = 0; i < actualTypeArguments.length; i++) {
			if(actualTypeArguments[i] == null) {
				actualTypeArguments[i] = getRawClass(typeParameters[i]);
			}
		}
		actualTypeArgumentsCache.put(extendType, actualTypeArguments);
		return actualTypeArguments;
	}
	
	/**
	 * 类继承检查过滤
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param rawType
	 * @param extendType
	 * @return
	 * @Description:
	 */
	private static Type checkTypeDirectCompatible(Class<?> rawType, Type extendType) {
		Class<?> extendClass = getRawClass(extendType);
		if(rawType == extendClass) {
			return null;
		}
		if(rawType.isInterface()) {
			for (Class<?> itemClass : extendClass.getInterfaces()) {
				if(rawType == itemClass) {
					return null;
				}
				if(org.apache.commons.lang3.ClassUtils.getAllInterfaces(itemClass).contains(rawType)) {
					return null;
				}
			}
		}
		return extendClass.getGenericSuperclass();
	}
	
	/**
	 * 接口实现检查过滤
	 * @Date:2015年9月11日
	 * @author wangk
	 * @param rawType
	 * @param extendType
	 * @return
	 * @Description:
	 */
	private static List<Type> getDirectCompatibleExtendLink(Class<?> rawType, Type extendType) {
		List<Type> extendLink = new ArrayList<Type>();
		extendLink.add(extendType);
		Class<?> extendClass = getRawClass(extendType);
		if(rawType == extendClass) {
			return extendLink;
		}
		for (Type itemType : extendClass.getGenericInterfaces()) {
			Class<?> itemClass = getRawClass(itemType);
			if(rawType == itemClass || org.apache.commons.lang3.ClassUtils.getAllInterfaces(itemClass).contains(rawType)) {
				extendLink.addAll(getDirectCompatibleExtendLink(rawType, itemType));
				break;
			}
		}
		return extendLink;
	}
	
	public static List<Method> getDeclearMethods(Class<?> clazz){
		return Arrays.asList(clazz.getDeclaredMethods());
	}

}
