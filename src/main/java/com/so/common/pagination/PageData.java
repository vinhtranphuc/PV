package com.so.common.pagination;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

public class PageData implements Pageable{

	private PageMapper pageMapper;
	private String sqlList;;

	public PageData(PageMapper pageMapper,MappedStatement mappedStatement, Object paramData)
			throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		this.pageMapper = pageMapper;
		BoundSql boundSql = mappedStatement.getBoundSql(paramData);
		this.sqlList = boundSql.getSql();
	}

	@Override
	public int getTotalElements() {
		return pageMapper.selectListTotalCnt(this.sqlList);
	}

	@Override
	public List<Map<String,Object>> getList(int startList, int pageSize) {
		return pageMapper.selectPageOfList(this.sqlList, startList, pageSize);
	}
}
