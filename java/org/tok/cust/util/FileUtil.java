package org.tok.cust.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.tok.view.Talk;


public class FileUtil {
	
	private static final Log logger = LogFactory.getLog(FileUtil.class);

	private static String uploadFilePath = Talk.getConfiguration().getString("enface.upload.dir");
	private static String downloadFilePath = Talk.getConfiguration().getString("enface.download.dir");
	
	
	public static String getUploadPath(){
		return uploadFilePath;
	}
	
	public static String getDownLoadPath(){
		return downloadFilePath;
	}
	
	public static String getDownLoadPath(String userFolder){
		return downloadFilePath + userFolder + "/";
	}
	
	/**
	 * 파일을 enview 폴더로 복사해주는 함수
	 */
	public static String uploadFile(String filePath, String saveImageFilePath, String fileName){
		BufferedInputStream bufferedInputStream = null;
		FileInputStream fileInputStream;
		File orizinalFile = new File(filePath);

		fileName = fileName.substring(0, fileName.length()-3) + StringUtils.lowerCase(fileName.substring(fileName.length()-3));
		BufferedOutputStream bufferedOutputStream = null;
		FileOutputStream fileOutputStream;
		File newFile = new File(Talk.getRealPath("") + saveImageFilePath + fileName);
		try {
			fileInputStream = new FileInputStream(orizinalFile);
			bufferedInputStream = new BufferedInputStream(fileInputStream);
			byte[] data = new byte[fileInputStream.available()];
			while(bufferedInputStream.read(data) != -1){}
			bufferedInputStream.close();
			
			fileOutputStream = new FileOutputStream(newFile);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(data);
			bufferedOutputStream.close();
			return fileName;
		}catch (FileNotFoundException e){
			new File(Talk.getRealPath("") + saveImageFilePath).mkdirs();
			return uploadFile(filePath, saveImageFilePath, fileName);
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}
	
	public static String uploadMultipartFile(String userFolder, MultipartFile multipartFile, boolean isUploadInstallFolder){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String fileName = "";
		if(multipartFile.getOriginalFilename() != null && !multipartFile.getOriginalFilename().equals("")){
			fileName = sdf.format(date) + "_" + multipartFile.getOriginalFilename();
		}
		else return "";
		File file = null;
		if(isUploadInstallFolder){
			file = new File(Talk.getRealPath("") + uploadFilePath + userFolder + "/" + fileName);
		}
		else file = new File(uploadFilePath + userFolder + "/" + fileName);
		
		logger.debug("FileUtil: uploadFilePath = " + file.getPath());
		logger.debug("FileUtil: multipartFile = " + multipartFile.getOriginalFilename());
		logger.debug("FileUtil: fileSize = " + multipartFile.getSize() + "bytes");
		try {
			multipartFile.transferTo(file);
			logger.debug("FileUtil: file.path = " + file.getPath());
			logger.debug("FileUtil: multipartFile transfer To file, Complete.");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			logger.debug("FileUtil: IOException = " + e);
			file.mkdirs();
			logger.debug("FileUtil: IOException -> Make Directory: " + file.getPath());
			return uploadMultipartFile(userFolder, multipartFile, isUploadInstallFolder);
		}
		return fileName;
	}
}
