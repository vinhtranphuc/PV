package com.so.common.pagination;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page<T> {

	int page = 1;
	int pageSize = 10;
	int totalPages;
	int totalElements;
	boolean isLast;
	private List<T> resultList;

	public Page(PageBuilder<T> pageBuilder) {
		this.page = pageBuilder.page;
		this.pageSize = pageBuilder.pageSize;
		this.totalElements = pageBuilder.totalElements;
		this.isLast = pageBuilder.isLast;
		this.resultList = pageBuilder.getResultList();
	}
}
