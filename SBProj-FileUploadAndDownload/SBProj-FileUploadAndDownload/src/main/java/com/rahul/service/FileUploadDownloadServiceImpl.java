package com.rahul.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadDownloadServiceImpl implements IFileUploadDownloadService {

	@Value("${file.upload.path}")
	private String uploadFileLoc;
	
	@Override
	public String saveFileToLocalFileSystem(MultipartFile multiPartFile) {
		String fileName="";
		Path uploadFilePath;
		
		//get the Original file name from input file
		fileName=StringUtils.cleanPath(multiPartFile.getOriginalFilename());
		
		// normalized the destination path
		uploadFilePath=Paths.get(uploadFileLoc).toAbsolutePath().normalize();
		
		//create the destination directory
		try {
			Files.createDirectories(uploadFilePath);
		} catch (IOException e) {
			throw new RuntimeException("Failed to Create Destination Directory",e);
		}
		
		//form the actual destination path
		Path filePath=Paths.get(uploadFilePath+"\\"+fileName);
		
		//copy/write  file to destination path
		try {
			Files.copy(multiPartFile.getInputStream(), filePath,StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new RuntimeException("Failed to copy file from source to destination",e);
		}
		
		return fileName;
	}
}
