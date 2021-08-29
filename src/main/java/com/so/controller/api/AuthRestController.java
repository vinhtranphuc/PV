package com.so.controller.api;

import com.so.common.BaseService;
import com.so.common.CookieUtils;
import com.so.exception.BusinessException;
import com.so.payload.request.LoginReq;
import com.so.payload.response.BODY;
import com.so.security.JwtTokenProvider;
import com.so.security.UserPrincipal;
import com.so.security.capcha.CaptchaGenerator;
import com.so.security.capcha.CaptchaUtils;
import com.so.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    HttpSession httpSession;

	@Autowired
    private CaptchaGenerator captchaGenerator;

    protected Logger logger = LoggerFactory.getLogger(AuthRestController.class);

	@PostMapping("login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginReq loginReq, HttpServletResponse response, HttpServletRequest request) {

        try {
            int failedLoginCnt = CaptchaUtils.getFailedLoginCnt(request);
            if (failedLoginCnt > 3) {

                Map<String, Object> result = new HashMap<String, Object>();

                String message = "";

                if (StringUtils.isEmpty(loginReq.getCaptcha())) {
                    message = "Please enther the captcha!";
                } else if (CaptchaUtils.isInvalidCaptcha(loginReq.getCaptcha(), request)) {
                    message = "Captcha does not match!";
                }

                if (StringUtils.isNotEmpty(message)) {
                    result.put("signal", "createCaptcha");
                    result.put("captchaEncode", CaptchaUtils.getNewCaptchaImg(captchaGenerator, request));
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BODY(result, message));
                }
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            CaptchaUtils.getFailedLoginCnt(request);

			if (authService.isAccountlDisabled(loginReq.getUsername())) {
				SecurityContextHolder.clearContext();
				HttpSession session = request.getSession(false);
                if (session != null)
                    session.removeAttribute("Authorization");
                CookieUtils.clear(response, "JSESSIONID");
                CookieUtils.clear(response, "remember-me");
				throw new BusinessException("Your account has been locked, please contact admin !");
			}

            if (loginReq.isRememberMe() == true) {
                // remember 30 days
                String cookieJwt = tokenProvider.generateTokenExp(authentication, 30);
                CookieUtils.addCookieJwtRememberMe(response, "remember-me", "Bearer " + cookieJwt, 2592000); // 30 days to seconds
            } else {
                CookieUtils.clear(response, "remember-me");
                String jwt = tokenProvider.generateTokenPrivate(authentication);
                httpSession.setAttribute("Authorization", "Bearer " + jwt);
            }

            return ResponseEntity.ok(new BODY(null, "You are successfully logged in"));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new BODY(null, e.getMessage()));
        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {

                int failedLoginCnt = CaptchaUtils.getFailedLoginCnt(request);
                CaptchaUtils.incrementFaiedLoginCnt(failedLoginCnt, request);

                Map<String, Object> result = new HashMap<String, Object>();
                if (failedLoginCnt >= 3) {
                    result.put("signal", "createCaptcha");
                    result.put("captchaEncode", CaptchaUtils.getNewCaptchaImg(captchaGenerator, request));
                }

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new BODY(result, "Username or password is incorrect. !"));
            } else {
                logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("current-user")
    public ResponseEntity<BODY> currentUser() {
        try {
            UserPrincipal userPrincipal = BaseService.getCurrentUser();

            Map<String, Object> result = new HashMap<String, Object>();
            result.put("user_id", userPrincipal.getId());
            result.put("full_name", userPrincipal.getFullName());
            String imageName = StringUtils.isNotEmpty(userPrincipal.getAvatarImg()) ? userPrincipal.getAvatarImg() : "DEFAULT.png";
            result.put("avatar_url", "/image/user-avatar/" + imageName + "/private");

            return ResponseEntity.ok().body(new BODY(result, "Get current user successfully !"));
        } catch (Exception e) {
            logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
