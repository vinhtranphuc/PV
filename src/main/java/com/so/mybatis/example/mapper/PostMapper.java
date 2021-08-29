package com.so.mybatis.example.mapper;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;

import com.so.payload.request.PostReq;

@Mapper
public interface PostMapper {

	Map<String, Object> selectPostById(Map<String, Object> postPrm);

	List<Map<String, Object>> selectPostList(Map<String,Object> params);

	List<Map<String, Object>> selectPostImagesById(String postId);

	int selectHomePostCnt(Map<String, Object> params);

	List<Map<String, Object>> selectHomePostList(Map<String, Object> params);

	Map<String, Object> selectHomePostById(Map<String, Object> params);

	List<Map<String, Object>>  selectSuggestPosts(Map<String, Object> params);

	List<Map<String, Object>>  selectPorpularPosts();

	void insertPost(PostReq postReq);

	void insertIgnorePostsTags(Map<String, Object> postTagPrm);

	void insertPostImages(Map<String, Object> postImagesPrm);

	void updatePost(@Valid PostReq postReq);

	void updatePostContentById(PostReq postReq);

	void updatePostLike(Map<String, Object> params);

	void deletePostImagesUnused(Map<String, Object> params);

	void deletePostTagsUnused(Map<String, Object> params);

	void deletePostTagsByPostId(Map<String, Object> params);

	void deletePostImagesByPostId(Map<String, Object> params);

	void deleteCommentsByPostId(Map<String, Object> params);

	void deletePosts(Map<String, Object> params);
}
