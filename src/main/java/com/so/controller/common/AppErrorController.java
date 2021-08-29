package com.so.controller.common;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * Basic Controller which is called for unhandled errors
 */
@Controller
@RequestMapping("error")
public class AppErrorController implements ErrorController {

    @RequestMapping(value = "")
    public String handleError(Model model, HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            if ((statusCode == HttpStatus.NOT_FOUND.value())) {
                return "/Errors/404";
            }
        }
        return "error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}