package com.rahul.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rahul.dto.FileUploadResponse;
import com.rahul.service.IFileUploadDwonloadService;

@RestController
public class FileUploadDownloadController {

	@Autowired
	private IFileUploadDwonloadService service;
	
	@PostMapping("/single/upload")
	public FileUploadResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
		String fileName="";
		String url="";
		String contentType="";
		FileUploadResponse response=null;
		
		//call B method to upload file
		fileName=service.uplodFileToLocalSystem(file);

		//get current context root of application
		url=ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/"+fileName).toUriString();
		//get conentType
		contentType=file.getContentType();

		//forming response object
		response=new FileUploadResponse(fileName, contentType, url);

		return response;
	}
	
}//class
