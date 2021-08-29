package com.so.common.pagination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class PageList<T> extends ArrayList<T> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public List<Map<String, Object>> mapList;
	public List<T> list;

	@SafeVarargs
	public PageList(T... typeInfo) {
		// Get generic type at runtime ...
		System.out.println(typeInfo.getClass().getComponentType().getTypeName());
	}

	public List<T> getList() {
		this.getClass().getGenericSuperclass();
//		return (List<T>) this.mapList.stream().map(t -> {
//			Type typeClass = T;
//			Class<?> clazz = GenereicUtil.getClass(typeClass);
//			return this.mapIgnoreNull(t, clazz);
//		}).collect(Collectors.toList());
		return null;
	}

	public List<Map<String, Object>> getMapList() {
		return mapList;
	}


}
