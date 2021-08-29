package com.so.controller.example.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.so.common.BaseService;
import com.so.common.pagination.Page;
import com.so.controller.common.BaseController;
import com.so.payload.request.PostReq;
import com.so.payload.response.BODY;
import com.so.security.UserPrincipal;
import com.so.service.example.PostService;

@RestController
@RequestMapping("/example/api/post")
public class PostRestController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(PostRestController.class);

	@Autowired
	private PostService postService;

	@GetMapping("/listPaging")
    public ResponseEntity<BODY> listPaging() {
        try {
        	UserPrincipal userPrincipal = BaseService.getCurrentUser();
        	Map<String,Object> params = new HashMap<String,Object>();
			params.put("role", userPrincipal.getAuthorities().iterator().next().toString());
			Page<Map<String,Object>> list = postService.getPostListPaging(params);
            return ResponseEntity.ok().body(new BODY(list, "Get list ok !"));
        } catch (Exception e) {
            logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

	@GetMapping("/list")
    public ResponseEntity<BODY> list() {
        try {
        	UserPrincipal userPrincipal = BaseService.getCurrentUser();
        	Map<String,Object> params = new HashMap<String,Object>();
			params.put("role", userPrincipal.getAuthorities().iterator().next().toString());
            List<Map<String,Object>> list = postService.getPostList(params);
            return ResponseEntity.ok().body(new BODY(list, "Get list ok !"));
        } catch (Exception e) {
            logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

	@PostMapping("/create")
	public ResponseEntity<BODY> createPost(@Valid @RequestBody PostReq postReq) {
		try {
			UserPrincipal userPrincipal = BaseService.getCurrentUser();
			postReq.setUser_id(userPrincipal.getId());
			//postForm.level = !isSupperAdmin()?"2":postForm.level;
			postReq.setLevel(!isSupperAdmin()?"2":postReq.getLevel());
			String postId = postService.createPost(postReq);
			return ResponseEntity.ok().body(new BODY(postId, "Create post successfully !"));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@PutMapping("/edit")
	public ResponseEntity<BODY> editPost(@Valid @RequestBody PostReq postReq) {
		try {
			UserPrincipal userPrincipal = BaseService.getCurrentUser();
			postReq.setUser_id(userPrincipal.getId());
			//postForm.level = !isSupperAdmin()?"2":postForm.level;
			postReq.setLevel(!isSupperAdmin()?"2":postReq.getLevel());
			postService.editPost(postReq);
			return ResponseEntity.ok().body(new BODY(postReq.getPost_id(), "Edit post successfully !"));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}

	@DeleteMapping("/delete")
	public ResponseEntity<BODY> deletePosts(@RequestBody Map<String,Object> params) {
		try {
			UserPrincipal userPrincipal = BaseService.getCurrentUser();
			params.put("role", userPrincipal.getAuthorities().iterator().next().toString());
			params.put("user_id", userPrincipal.getId());
			postService.deletePosts(params);
			return ResponseEntity.ok().body(new BODY(null, "Delete posts successfully !"));
		} catch (Exception e) {
			logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
