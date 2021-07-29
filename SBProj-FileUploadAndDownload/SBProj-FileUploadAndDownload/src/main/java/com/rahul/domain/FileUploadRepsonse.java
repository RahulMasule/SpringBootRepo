package com.rahul.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileUploadRepsonse {

	private String fileName;
	private String fileType;
	private String url;
}
