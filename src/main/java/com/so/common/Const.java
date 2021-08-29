package com.so.common;

public class Const {

    // PREFIX URL
    public static final String STORE_ROOT = "store";
    public static final String FILE_COMMON_DIR = "store/common";
    public static final String FILE_UPLOAD_DIR = "store/upload";
    private static final String USER_AVATAR_DIR = "/user/%s/avatar";
    private static final String POST_THUMBNAIL_DIR = "/post/%s/thumbnail";
    private static final String POST_CONTENT_IMAGES_DIR = "/post/%s/content_images";
    private static final String POST_DIR = "/post/%s";

    // max captcha tries
    public static final Integer MAX_CAPTCHA_TRIES = 3;

    public final static String GET_USER_AVATAR_DIR(String userName) {
        return String.format(USER_AVATAR_DIR, userName);
    }

    public final static String GET_POST_THUMBNAIL_DIR(String postId) {
    	return String.format(POST_THUMBNAIL_DIR, postId);
    }

    public final static String GET_POST_CONTENT_IMAGES_DIR(String postId) {
        return String.format(POST_CONTENT_IMAGES_DIR, postId);
    }

    public final static String GET_POST_DIR(String postId) {
        return String.format(POST_DIR, postId);
    }
}
