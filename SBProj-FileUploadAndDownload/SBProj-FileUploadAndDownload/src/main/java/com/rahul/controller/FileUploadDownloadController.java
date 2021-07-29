package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rahul.domain.FileUploadRepsonse;
import com.rahul.service.IFileUploadDownloadService;

@RestController
public class FileUploadDownloadController {

	@Autowired
	private IFileUploadDownloadService service;
	
	@PostMapping("/upload/singleFile")
	public FileUploadRepsonse saveFileToLocalFileSystem(@RequestParam("file") MultipartFile multiPartFile) {
		FileUploadRepsonse response=null;
		String url="";
		String contentType="";

		//call B method to upload given input file to local file system
		String fileName= service.saveFileToLocalFileSystem(multiPartFile);
		//forming url
		url=ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/"+fileName).toUriString();
		//forming contentType
		contentType=multiPartFile.getContentType();
		//forming final response
		response=new FileUploadRepsonse(fileName, contentType, url);
		return response;
	}
}
