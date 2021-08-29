package com.so.controller.common;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.so.common.FileUtils;
import com.so.exception.ResourceNotFoundException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("store")
public class StoreController {

    @Autowired
    private FileUtils fileUtils;

    protected Logger logger = LoggerFactory.getLogger(BaseController.class);

    @GetMapping("/upload/**/{filename:.+}")
    public ResponseEntity<Resource> getUploadFile(@PathVariable String filename, HttpServletRequest request) {
        try {
            String subPath = getSubPath(request, true);
            Resource file = fileUtils.getUploadResourceByPath(subPath, filename);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (ResourceNotFoundException ex) {
            if (FileUtils.isImageFile(ex.getFieldName())) {
                Resource noPhotoImg = fileUtils.getCommonResourceByPath("", "no-photo.png");
                return new ResponseEntity<>(noPhotoImg, new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            logger.error("ResourceNotFoundException : {}", ExceptionUtils.getStackTrace(ex));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/common/**/{filename:.+}")
    public ResponseEntity<Resource> getCommonFile(@PathVariable String filename, HttpServletRequest request) {
        try {
            String subPath = getSubPath(request, false);
            Resource file = fileUtils.getCommonResourceByPath(subPath, filename);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        } catch (ResourceNotFoundException ex) {
            if (FileUtils.isImageFile(ex.getFieldName())) {
                Resource noPhotoImg = fileUtils.getCommonResourceByPath("", "no-photo.png");
                return new ResponseEntity<>(noPhotoImg, new HttpHeaders(), HttpStatus.NOT_FOUND);
            }
            logger.error("ResourceNotFoundException : {}", ExceptionUtils.getStackTrace(ex));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            logger.error("Excecption : {}", ExceptionUtils.getStackTrace(e));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private String getSubPath(HttpServletRequest request, boolean isUpload) {
        String requestUri = request.getRequestURI();
        String[] requestUriArr = requestUri.split("/store/" + (isUpload ? "upload" : "common"
                + "") + "/");
        String requestMapping = requestUriArr[requestUriArr.length - 1];
        int subPathIndex = requestMapping.lastIndexOf('/');
        return subPathIndex > 0 ? requestMapping.substring(0, subPathIndex) : "";
    }
}
