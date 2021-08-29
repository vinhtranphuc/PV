package com.so.mybatis.example.mapper;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Resource
public interface TagMapper {

	List<Map<String, Object>> selectTags();

	List<Map<String, Object>> selectTagsByPostId(String postId);

	Map<String,Object> selectTagById(String tagId);

	void insertIgnoreTag(Map<String, Object> tagPrm);

	void deleteTagsUnused();
}
