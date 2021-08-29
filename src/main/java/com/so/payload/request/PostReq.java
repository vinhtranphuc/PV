package com.so.payload.request;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class PostReq {
	
	private String post_id;

	@Size(min=0,max=100,message = "Please enter at least 100 characters for the title!")
	private String title;

	private String category_id;
	private String language;
	private String content;
	private String level;
	private String summary;
	private String published_at;
	private List<String> thumbnailList;
	private List<String> tagList;
	private Integer hasImagesOntop;
	private Integer imagesLayout;

	private Long user_id;
	private Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
}
