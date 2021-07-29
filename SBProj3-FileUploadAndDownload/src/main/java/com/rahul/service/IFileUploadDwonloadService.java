package com.rahul.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileUploadDwonloadService {

	public String uplodFileToLocalSystem(MultipartFile file);
}
