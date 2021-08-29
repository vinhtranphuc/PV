package com.so.common.pagination;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PageMapper {

	@Select("SELECT\r\n"
			+ "	 COUNT(*)\r\n"
			+ "FROM (\r\n"
			+ "  ${sqlList}\r\n"
			+ ") t")
	public int selectListTotalCnt(@Param("sqlList") String sqlList);

	@Select("${sqlList}\r\n"
			+ "	LIMIT #{startList},#{pageSize}")
	public List<Map<String,Object>> selectPageOfList(@Param("sqlList") String sqlList, int startList, int pageSize);
}
