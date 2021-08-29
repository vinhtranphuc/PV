package com.so.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.so.controller.common.BaseController;
import com.so.security.capcha.CaptchaGenerator;
import com.so.security.capcha.CaptchaUtils;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

	@Autowired
    private CaptchaGenerator captchaGenerator;

    protected Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String base(Model model) {
        return "redirect:/auth/login";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(Model model, HttpServletRequest request) {
        if (isAuthenticated()) {
            return "redirect:/example/post/list";
        }
        int failedLoginCnt = CaptchaUtils.getFailedLoginCnt(request);
        if (failedLoginCnt > 3) {
            model.addAttribute("captchaEncode", CaptchaUtils.getNewCaptchaImg(captchaGenerator, request));
            model.addAttribute("isShowCaptcha", true);
        }
        return "/auth/login";
    }
}