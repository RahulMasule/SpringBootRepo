package com.rahul.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
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
		//forming url eg.http://localhost:8081/download/rahul.jpg
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
	
	@PostMapping("/upload/multipleFiles")
	public List<FileUploadRepsonse> UploadMultipleFiles(@RequestParam("files") MultipartFile[] multiPartFiles) {
		
		if(multiPartFiles.length>7)
			throw new RuntimeException("Too Many Files");
		
		List<FileUploadRepsonse> fileUploadReList=new ArrayList<FileUploadRepsonse>();
		Arrays.asList(multiPartFiles)
					.stream()
					.forEach(file->{
						//call B method to upload given input file to local file system
						String fileName= service.saveFileToLocalFileSystem(file);
						//forming url eg.http://localhost:8081/download/rahul.jpg
						String url=ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/"+fileName).toUriString();
						//forming contentType
						String contentType=file.getContentType();
						//forming final response
						FileUploadRepsonse response=new FileUploadRepsonse(fileName, contentType, url);
						fileUploadReList.add(response);
					});
		return fileUploadReList;
	}
	
	@GetMapping("/downloadMultipleeIntoZip")
	public void downloadMultipleFileIntoZip(@RequestParam("fileName") String [] files,HttpServletResponse resp) throws IOException {
	
		String zipFileName="ZipFile.zip";
		//Open ZipOutputStream to write data
		try(ZipOutputStream zos=new ZipOutputStream(resp.getOutputStream())) {
			
			Arrays.asList(files)
						.stream()
						.forEach(file->{
							//fetch single file at a time from file system location
							Resource resource=service.downloadFileFromLocalFileSystem(file);
							//create single zip entry at a time for fetch resource from FileSystem 
							ZipEntry zipEntry=new ZipEntry(resource.getFilename());
							try {
								zipEntry.setSize(resource.contentLength());
								zos.putNextEntry(zipEntry);
								//copy content to file to destination using ZipOutputSteam
								StreamUtils.copy(resource.getInputStream(), zos);
								zos.closeEntry();
							} catch (IOException e) {
								System.out.println("Exception while zipping file");
								e.printStackTrace();
							}
						});
			zos.finish();
		}
		resp.setStatus(200);
		resp.addHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment;fileName="+zipFileName);
	}
}
