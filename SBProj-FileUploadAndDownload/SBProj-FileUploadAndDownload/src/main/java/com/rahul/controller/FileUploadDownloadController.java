package com.rahul.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName,HttpServletRequest req) {
		Resource resource=null;
		String mimeType="";
		resource=service.downloadFileFromLocalFileSystem(fileName);
		
		//making dynamic contentType of file
		try {
			mimeType=req.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException e) {
			mimeType=MediaType.APPLICATION_OCTET_STREAM_VALUE;
		}
		mimeType=mimeType==null?MediaType.APPLICATION_OCTET_STREAM_VALUE:mimeType;
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(mimeType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName="+resource.getFilename())//to download
//				.header(HttpHeaders.CONTENT_DISPOSITION, "inline;filename="+resource.getFilename())//to render
				.body(resource);
	}
}
