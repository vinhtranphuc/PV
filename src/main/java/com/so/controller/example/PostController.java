package com.so.controller.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.so.common.BaseService;
import com.so.controller.common.BaseController;
import com.so.security.UserPrincipal;
import com.so.service.example.CategoryService;
import com.so.service.example.PostService;
import com.so.service.example.TagService;

@Controller
@RequestMapping("/example/post")
public class PostController extends BaseController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private TagService tagService;

	@Autowired
	private PostService postService;

	@RequestMapping(value = {"list", "list/"}, method = RequestMethod.GET)
    public String list(Model model) {
		List<Map<String,Object>> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);
        return "example/post/list";
    }

	@RequestMapping(value = {"list-autofilter", "list-autofilter/"}, method = RequestMethod.GET)
    public String listAutoFilter(Model model) {
		List<Map<String,Object>> categories = categoryService.getCategories();
		model.addAttribute("categories", categories);
        return "example/post/list-autofilter";
    }

	@RequestMapping(value = {"create", "create/"}, method = RequestMethod.GET)
    public String create(Model model) {

		model.addAttribute("categories", categoryService.getCategories());
		List<Map<String,Object>> tags = tagService.getTags();
		List<String> tagNameList = tags.stream().map(t-> {
			return (String) t.get("tag");
		}).collect(Collectors.toList());
		String tagsStr = org.apache.tomcat.util.buf.StringUtils.join(tagNameList);

		model.addAttribute("tagsStr", tagsStr);
		model.addAttribute("isSupperAdmin", isSupperAdmin());

        return "example/post/create";
    }

	@RequestMapping(value = {"edit", "edit/"}, method = RequestMethod.GET)
    public String edit(@RequestParam Map<String,Object> params, Model model) {

		if(StringUtils.isEmpty((String) params.get("postId"))) {
			return "Errors/404";
		}

		UserPrincipal userPrincipal = BaseService.getCurrentUser();
		params.put("role", userPrincipal.getAuthorities().iterator().next().toString());
		Map<String,Object> post = postService.getPostById(params, false);
		if(post == null || post.isEmpty()) {
			return "Errors/404";
		}

		List<Map<String,Object>> categories = categoryService.getCategories();
		List<Map<String,Object>> postCategories = categories.stream().map(t-> {
			if(StringUtils.equals(t.get("category_id")+"", post.get("category_id")+"")) {
				t.put("isSelected", true);
			} else {
				t.put("isSelected", false);
			}
			return t;
		}).collect(Collectors.toList());

		model.addAttribute("categories", postCategories);

		List<Map<String,Object>> tags = tagService.getTags();
		List<String> tagNameList = tags.stream().map(t-> {
			return (String) t.get("tag");
		}).collect(Collectors.toList());
		String tagsStr = org.apache.tomcat.util.buf.StringUtils.join(tagNameList);

		model.addAttribute("tagsStr", tagsStr);
		model.addAttribute("isSupperAdmin", isSupperAdmin());
		model.addAttribute("post", post);

        return "example/post/edit";
    }
}
