package com.so.common.pagination;

import java.util.List;
import java.util.Map;

public interface Pageable {

	public int getTotalElements();

	public List<Map<String,Object>> getList(int startList, int pageSize);
}
