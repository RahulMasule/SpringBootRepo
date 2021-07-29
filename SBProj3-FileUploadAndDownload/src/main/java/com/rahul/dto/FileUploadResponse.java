package com.rahul.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadResponse {
	
	private String filename;
	private String contentType;
	private String url;

}
