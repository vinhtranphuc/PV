package com.so.service.example;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.so.common.Const;
import com.so.common.FileUtils;
import com.so.common.pagination.Page;
import com.so.common.pagination.PageBuilder;
import com.so.mybatis.example.mapper.PostMapper;
import com.so.mybatis.example.mapper.TagMapper;
import com.so.payload.request.PostReq;


@Service
public class PostService {

	@Autowired
	private FileUtils fileUtils;
	@Resource
	private PostMapper postMapper;
	@Resource
	private TagMapper tagMapper;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public Map<String, Object> getPostById(Map<String, Object> params, boolean isHome) {
		String postId = (String) params.get("postId");
		Map<String, Object> post;
		if (isHome) {
			post = postMapper.selectHomePostById(params);
		} else {
			post = postMapper.selectPostById(params);
		}
		if (post == null || post.isEmpty()) {
			return null;
		}
		List<Map<String, Object>> postTags = tagMapper.selectTagsByPostId(postId);
		post.put("postTags", postTags);
		String postTagsStr = "";
		if (postTags.size() > 0) {
			postTagsStr = org.apache.tomcat.util.buf.StringUtils.join(postTags.stream().map(t -> {
				return (String) t.get("tag");
			}).collect(Collectors.toList()));
		}
		post.put("postTagsStr", postTagsStr);
		List<Map<String, Object>> postImages = postMapper.selectPostImagesById(postId);
		post.put("postImages", postImages);
		return post;
	}

//	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED)
	public String createPost(PostReq postReq) throws IOException {

		postMapper.insertPost(postReq);
		// postForm.content = convertContentImg(true,postForm.content,postForm.post_id);
		postReq.setContent(convertContentImg(true, postReq.getContent(), postReq.getPost_id()));
		postMapper.updatePostContentById(postReq);
		createPostTags(postReq.getTagList(), postReq.getPost_id());
		if (postReq.getThumbnailList() != null && postReq.getThumbnailList().size() > 0) {
			Map<String, Object> postImagesPrm = new HashMap<String, Object>();
			postImagesPrm.put("post_id", postReq.getPost_id());
			postReq.getThumbnailList().stream().filter(t -> StringUtils.isNotEmpty(t)).forEach(t -> {
				String imagePath = "";
				try {
					imagePath = fileUtils.uploadBase64File(t, "", Const.GET_POST_THUMBNAIL_DIR(postReq.getPost_id()));
					String standardImagePath = fileUtils.resizeImage(585, 390, imagePath);
					String smallImagePath = fileUtils.resizeImage(263, 175, imagePath);

					postImagesPrm.put("image_path", imagePath);
					postImagesPrm.put("standard_image_path", standardImagePath);
					postImagesPrm.put("small_image_path", smallImagePath);
					postMapper.insertPostImages(postImagesPrm);
				} catch (UnsupportedEncodingException e) {
					logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
				}
			});
		}

		return postReq.getPost_id();
	}

//	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED)
	public void editPost(@Valid PostReq postReq) throws IOException {

		// postForm.content =
		// convertContentImg(false,postForm.content,postForm.post_id);
		postReq.setContent(convertContentImg(false, postReq.getContent(), postReq.getPost_id()));
		postMapper.updatePost(postReq);

		createPostTags(postReq.getTagList(), postReq.getPost_id());

		if (postReq.getThumbnailList() != null && postReq.getThumbnailList().size() > 0) {
			Map<String, Object> postImagesPrm = new HashMap<String, Object>();
			postImagesPrm.put("post_id", postReq.getPost_id());

			List<String> thumbnailPathList = postReq.getThumbnailList().stream().filter(t -> StringUtils.isNotEmpty(t))
					.map(t -> {
						if (t.startsWith("data:")) {
							try {
								String imagePath = fileUtils.uploadBase64File(t, "",
										Const.GET_POST_THUMBNAIL_DIR(postReq.getPost_id()));
								String standardImagePath = fileUtils.resizeImage(585, 390, imagePath);
								String smallImagePath = fileUtils.resizeImage(263, 175, imagePath);

								postImagesPrm.put("image_path", imagePath);
								postImagesPrm.put("standard_image_path", standardImagePath);
								postImagesPrm.put("small_image_path", smallImagePath);
								postMapper.insertPostImages(postImagesPrm);
								return imagePath;
							} catch (UnsupportedEncodingException e) {
								logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
							}
						}
						return t;
					}).collect(Collectors.toList());
			postImagesPrm.put("thumbnailPathList", thumbnailPathList);
			postMapper.deletePostImagesUnused(postImagesPrm);
			fileUtils.synchronizeFilesToData(true, Const.GET_POST_THUMBNAIL_DIR(postReq.getPost_id()), thumbnailPathList);
		}
	}

	private String convertContentImg(boolean isCreateNew, String content, String postId) throws IOException {
		Document doc = Jsoup.parse(content, "UTF-8");
		if (isCreateNew) {
			for (Element element : doc.select("img")) {
				String src = element.attr("src");
				if (StringUtils.isEmpty(src))
					continue;
				String filePath = "";
				if (src.startsWith("data:")) {
					filePath = fileUtils.uploadBase64File(src, "", Const.GET_POST_CONTENT_IMAGES_DIR(postId));
				}
				element.attr("src", filePath);
			}
		} else {
			List<String> filePathList = new ArrayList<String>();
			for (Element element : doc.select("img")) {
				String src = element.attr("src");
				if (StringUtils.isEmpty(src))
					continue;
				String filePath = "";
				if (src.startsWith("data:")) {
					filePath = fileUtils.uploadBase64File(src, "", Const.GET_POST_CONTENT_IMAGES_DIR(postId));
				} else {
					filePath = src;
				}
				element.attr("src", filePath);
				filePathList.add(filePath);
			}
			fileUtils.synchronizeFilesToData(true, Const.GET_POST_CONTENT_IMAGES_DIR(postId), filePathList);
		}
		return doc.body().children().toString();
	}

//	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED)
	private void createPostTags(List<String> tagList, String postId) {
		if (tagList != null) {
			List<String> tagIdList = new ArrayList<String>();
			tagList.forEach(t -> {
				if (StringUtils.isNotEmpty(t)) {
					Map<String, Object> tagPrm = new HashMap<String, Object>();
					tagPrm.put("tag", t);
					tagMapper.insertIgnoreTag(tagPrm);
					String tagIdInserted = (String) tagPrm.get("tag_id");
					tagIdList.add(tagIdInserted);
				}
			});
			if (tagIdList.size() > 0) {
				Map<String, Object> postTagPrm = new HashMap<String, Object>();
				postTagPrm.put("post_id", postId);
				tagIdList.forEach(t -> {
					postTagPrm.put("tag_id", t);
					postMapper.insertIgnorePostsTags(postTagPrm);
				});
				postTagPrm.put("tagIdList", tagIdList);
				postMapper.deletePostTagsUnused(postTagPrm);
				tagMapper.deleteTagsUnused();
			}
		}
	}

	public Page<Map<String,Object>> getPostListPaging(Map<String, Object> params) {
		params.put("xxx", 1);
		Object[] params1 = {params};
		Page<Map<String,Object>> postsPage = new PageBuilder<Map<String,Object>>(postMapper, "selectPostList",params1).withPage(1).withPageSize(2).build();
		System.out.println(postsPage);
		return postsPage;
	}

	public List<Map<String, Object>> getPostList(Map<String, Object> params) {
		List<Map<String, Object>> postList = postMapper.selectPostList(params);
		postList.forEach(t -> {
			List<Map<String, Object>> thumbnails = postMapper.selectPostImagesById(t.get("post_id") + "");
			if (thumbnails != null && thumbnails.size() > 0) {
				t.put("thumbnails", thumbnails);
				t.put("first_thumbnail", thumbnails.get(0));
			} else {
				t.put("thumbnails", "");
				t.put("first_thumbnail", "");
			}
		});
		return postList;
	}

	@SuppressWarnings("unchecked")
//	@Transactional(value = "transactionManager", isolation = Isolation.READ_COMMITTED)
	public void deletePosts(Map<String, Object> params) {
		postMapper.deletePostTagsByPostId(params);
		postMapper.deletePostImagesByPostId(params);
		postMapper.deleteCommentsByPostId(params);
		postMapper.deletePosts(params);
		tagMapper.deleteTagsUnused();
		List<String> postIdList = (List<String>) params.get("postIdArr");
		postIdList.forEach(t -> {
			FileUtils.deleteDirectory(Const.FILE_UPLOAD_DIR + Const.GET_POST_DIR(t));
		});
	}

	private List<Map<String, Object>> addImagesToPost(List<Map<String, Object>> posts) {
		posts.forEach(t -> {
			List<Map<String, Object>> thumbnails = postMapper.selectPostImagesById(t.get("post_id") + "");
			if (thumbnails != null && thumbnails.size() > 0) {
				t.put("thumbnails", thumbnails);
				t.put("first_thumbnail", thumbnails.get(0));
			} else {
				t.put("thumbnails", "");
				t.put("first_thumbnail", "");
			}
		});
		return posts;
	}

	public int handleLike(Map<String, Object> params, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String postSessionAttr = "LikePostId" + params.get("postId");
		String status = "";
		if (session.getAttribute(postSessionAttr) == null) {
			status = "like";
		} else {
			status = session.getAttribute(postSessionAttr) + "";
		}
		if (!StringUtils.equals("like", status)) {
			status = "unlike";
		}
		params.put("status", status);
		postMapper.updatePostLike(params);
		session.setAttribute(postSessionAttr, StringUtils.equals("like", status) ? "unlike" : "like");
		int likeCnt = params.get("like_cnt") == null ? 0 : (Integer.parseInt(params.get("like_cnt") + ""));
		return likeCnt;
	}

	public List<Map<String, Object>> getSuggestPosts(Map<String, Object> post) {
		List<Map<String, Object>> list = postMapper.selectSuggestPosts(post);
		addImagesToPost(list);
		return list;
	}
}
