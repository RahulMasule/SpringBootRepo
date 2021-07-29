package com.rahul.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadDownloadService {

	String saveFileToLocalFileSystem(MultipartFile multiPartFile);

}
