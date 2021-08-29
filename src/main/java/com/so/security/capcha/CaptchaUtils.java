package com.so.security.capcha;

import nl.captcha.Captcha;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class CaptchaUtils {

    public static String encodeBase64(Captcha captcha) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(captcha.getImage(), "png", outputStream);
            return DatatypeConverter.printBase64Binary(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getFailedLoginCnt(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        return (httpSession != null && httpSession.getAttribute("failed-login-cnt") != null)
                ? (int) httpSession.getAttribute("failed-login-cnt")
                : 0;
    }

    public static void clearFailedLoginCnt(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("failed-login-cnt");
    }

    public static void incrementFaiedLoginCnt(int failedLoginCnt, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("failed-login-cnt", (failedLoginCnt += 1));
    }

    public static String getNewCaptchaImg(CaptchaGenerator captchaGenerator, HttpServletRequest request) {
        Captcha captcha = captchaGenerator.createCaptcha(150, 45);
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("captcha", captcha);
        return CaptchaUtils.encodeBase64(captcha);
    }

    public static boolean isInvalidCaptcha(String captchaAnswer, HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        if (httpSession == null || httpSession.getAttribute("captcha") == null)
            return false;
        Object object = httpSession.getAttribute("captcha");
        Captcha captcha = (object instanceof Captcha) ? (Captcha) object : null;

        return captcha==null || !captcha.getAnswer().equals(captchaAnswer);
    }
}
