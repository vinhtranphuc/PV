
package com.so.common;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.so.exception.ResourceNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileUtils {

    protected static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * upload MultipartFile
     * @param fileContent
     * @param subDirectory
     * @return
     * @throws UnsupportedEncodingException
     */
    public String uploadFile(MultipartFile fileContent, String subDirectory)
            throws UnsupportedEncodingException {

        Path folderPath = getFileUploadDir(subDirectory);
        String fileName = generateFileName(fileContent.getOriginalFilename());
        String filePath = "/" + folderPath + "/" + fileName;

        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            InputStream inputStream = fileContent.getInputStream();
            Files.copy(inputStream, folderPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return  StringUtils.replace(filePath, "\\", "/");
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        } finally {
            System.gc();
        }
        return null;
    }

    /**
     * upload MultipartFile
     * @param fileContent
     * @param subDirectory
     * @param storeKey
     * @return
     * @throws UnsupportedEncodingException
     */
    public String uploadFile(MultipartFile fileContent, String subDirectory, String storeKey)
            throws UnsupportedEncodingException {

        Path folderPath = getFileUploadDir(subDirectory, storeKey);
        String fileName = generateFileName(fileContent.getOriginalFilename());
        String filePath = "/" + folderPath + "/" + fileName;

        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            InputStream inputStream = fileContent.getInputStream();
            Files.copy(inputStream, folderPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

            return  StringUtils.replace(filePath, "\\", "/");
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        } finally {
            System.gc();
        }
        return null;
    }

    /**
     * upload base64 file
     * @param base64Image
     * @param crrfileName
     * @param childDirectory
     * @param storeKey
     * @return
     * @throws UnsupportedEncodingException
     */
    public String uploadBase64File(String base64Image, String crrfileName, String childDirectory, String storeKey)
            throws UnsupportedEncodingException {

        Path folderPath = getFileUploadDir(childDirectory, storeKey);
        String fileName = generateFileName(crrfileName);
        String filePath = "/" + folderPath + "/" + fileName;

        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String encodedImg = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            InputStream inputStream = new ByteArrayInputStream(decodedImg);
            Files.copy(inputStream, folderPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return  StringUtils.replace(filePath, "\\", "/");
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        } finally {
            System.gc();
        }
        return null;
    }

    public String uploadBase64File(String base64Image, String crrfileName, String subDirectory)
            throws UnsupportedEncodingException {

        Path folderPath = getFileUploadDir(subDirectory);
        String fileName = generateFileName(crrfileName);
        String filePath = "/" + folderPath + "/" + fileName;

        try {
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String encodedImg = base64Image.contains(",") ? base64Image.split(",")[1] : base64Image;
            byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
            InputStream inputStream = new ByteArrayInputStream(decodedImg);
            Files.copy(inputStream, folderPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
            return  StringUtils.replace(filePath, "\\", "/");
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        } finally {
            System.gc();
        }
        return null;
    }

    public static Path getFileUploadDir(String subDirectory, String storeKey) {
        Path filePath = Paths.get(Const.FILE_UPLOAD_DIR, subDirectory, storeKey);
        return filePath;
    }

    public static Path getFileUploadDir(String subDirectory) {
        Path filePath = Paths.get(Const.FILE_UPLOAD_DIR, subDirectory);
        return filePath;
    }

    public static Path getFileCommonDir(String subDirectory) {
        Path filePath = Paths.get(Const.FILE_COMMON_DIR, subDirectory);
        return filePath;
    }

    public String generateFileName(String originalFileName) throws UnsupportedEncodingException {
        String fileName = Utils.createTimeId();
        if(StringUtils.isNotEmpty(originalFileName)) {
            fileName = Utils.createTimeId() + "_" + originalFileName.toLowerCase();
        }else {
            fileName+=fileName+".png";
        }
        return java.net.URLEncoder.encode(StringUtils.deleteWhitespace(fileName), "UTF-8");
    }

    public Resource getUploadResourceByPath(String subDirectory, String fileName) {
        Path filePath = getFileUploadDir(subDirectory);
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.resolve(java.net.URLEncoder.encode(fileName, "UTF-8")).toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException(resource, fileName);
            }
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        }
        return resource;
    }

    public Resource getCommonResourceByPath(String subDirectory, String fileName) {
        Path filePath = getFileCommonDir(subDirectory);
        Resource resource = null;
        try {
            resource = new UrlResource(filePath.resolve(java.net.URLEncoder.encode(fileName, "UTF-8")).toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException(resource, fileName);
            }
        } catch (IOException e) {
            logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
        }
        return resource;
    }

    public boolean deleteByPath(String fullPath) {
        try {
        	System.gc();
            Path delPath = Paths.get(fullPath);
            Files.deleteIfExists(delPath);
            return true;
        } catch (Exception e) {
        	logger.error("Exception : {}", ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
    
    public static void deleteDirectory(String directory) {
    	recursiveDelete(new File(directory));
    }
    
    private static void recursiveDelete(File file) {
        //to end the recursive loop
        if (!file.exists())
            return;
        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
    }

    public static boolean isImageFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        String regex = "([^/s]+(/.(?i)(jpe?g|png|gif|bmp|svg))$)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(fileName);
        return m.matches();
    }
    
	public void synchronizeFilesToData(boolean isUploadDir, String subDir, List<String> filePathList)
			throws IOException {
		// get full directory
		String fullDir = (isUploadDir ? Const.FILE_UPLOAD_DIR : Const.FILE_COMMON_DIR) + subDir;
		
		if(filePathList.size()<1 && StringUtils.isNoneEmpty(subDir)) {
			deleteDirectory(fullDir);
			return;
		}

		// check file list
		List<String> outsideList = new ArrayList<String>();
		for (String filePath : filePathList) {
			
			int startIndex = StringUtils.equals("/",filePath.charAt(0)+"")?1:0;
			String checkDir = filePath.substring(startIndex, fullDir.length()+1);
			
			if (!StringUtils.equals(fullDir, checkDir)) {
				logger.warn("Exists a file outside of the sync directory :");
				outsideList.add(filePath);
				//throw new AppException("Exists a file outside of the sync directory :" + filePath);
			}
		}
		// remove files outside
		outsideList.forEach(t-> {
			filePathList.remove(t);
		});

		// convert file path list to file name list
		List<String> fileNameList = filePathList.stream().map(t -> {
			File file = new File(t);
			return file.getName();
		}).collect(Collectors.toList());

		List<String> existFiles = getFilesInnerDir(isUploadDir, subDir);
		List<String> trashFiles = existFiles;
		trashFiles.parallelStream().filter(t-> {
			for(String e:fileNameList) {
				if(t.toLowerCase().contains(e)) {
					return false;
				}
			}
			return true;
		}).collect(Collectors.toList());

		System.gc();
		trashFiles.stream().filter(e -> e != null).collect(Collectors.toList());
		if (!trashFiles.isEmpty()) {
			trashFiles.stream().forEach(t -> {
				String delFilePath = fullDir.concat("/".concat(t));
				deleteByPath(delFilePath);
			});
		}
	}

	public static List<String> getFilesInnerDir(boolean isUploadDir, String subDir) throws IOException {
		Stream<Path> streamPath = null;
		try {
			streamPath = Files.walk(Paths.get(isUploadDir ? Const.FILE_UPLOAD_DIR : Const.FILE_COMMON_DIR, subDir));
		} catch (NoSuchFileException ex) {
			logger.error("Exception : {}", ExceptionUtils.getStackTrace(ex));
			return new ArrayList<String>();
		}
		
		try (Stream<Path> stream = streamPath) {
			return stream.filter(file -> !Files.isDirectory(file)).map(Path::getFileName).map(Path::toString)
					.collect(Collectors.toList());
		}
	}
	
	public String resizeImage(int width, int height, String filePath) {
		
		filePath = StringUtils.replace(filePath, "\\", "/");
		int startIndex = StringUtils.equals("/",filePath.charAt(0)+"")?1:0;
		String correctPath = filePath.substring(startIndex, filePath.length());
		
		File file = new File(correctPath);
		String fileName = file.getName();
		
		String outPutFileNamePartern = "%sx%s-%s";
		String outPutFileName = String.format(outPutFileNamePartern, width, height, fileName);
		String outPutDir = correctPath.substring(0,correctPath.lastIndexOf("/")+1);
		String outPutPath = outPutDir.concat(outPutFileName);
		
		try {
			Thumbnails.of(file)
					.size(width, height).outputQuality(0.80).toFile(outPutPath);
		} catch (IOException e) {
			logger.error("IOException : {}", ExceptionUtils.getStackTrace(e));
		}
		return "/".concat(outPutPath);
	}
}
