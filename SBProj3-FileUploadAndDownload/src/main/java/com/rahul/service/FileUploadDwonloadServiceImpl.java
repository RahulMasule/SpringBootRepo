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
public class FileUploadDwonloadServiceImpl implements IFileUploadDwonloadService {

	@Value("${file.storage.location}")
	private String storageFolderPath;

	@Override
	public String uplodFileToLocalSystem(MultipartFile multiPartFile) {
		String fileName = "";
		Path storagelocationPath;
		
		try {
			// get the fileName
			fileName = StringUtils.cleanPath(multiPartFile.getOriginalFilename());

			// reading folder path(in String) from application.props file and making folder path URI
			storagelocationPath = Paths.get(storageFolderPath).toAbsolutePath().normalize();

			// create the directory
			Files.createDirectories(storagelocationPath);
			
			//locate the uploadFolder Path
			Path actualfilePath=Paths.get(storagelocationPath+"\\"+fileName);

			//copy the file to uploadFolder location
			Files.copy(multiPartFile.getInputStream(), actualfilePath, StandardCopyOption.REPLACE_EXISTING);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
	}

}
