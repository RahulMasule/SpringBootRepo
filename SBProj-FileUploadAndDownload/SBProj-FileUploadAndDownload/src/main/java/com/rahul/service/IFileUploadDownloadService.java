package com.rahul.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadDownloadService {

	String saveFileToLocalFileSystem(MultipartFile multiPartFile);

	Resource downloadFileFromLocalFileSystem(String fileName);

}
