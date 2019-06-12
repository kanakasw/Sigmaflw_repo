package com.data.integration.service.vo;

import org.springframework.web.multipart.MultipartFile;

public class BatchProcessVO {

	private String subscriberUniqueKey;
	private String processUniqueKey;
	private String fileUniqueKey;
	private String fileName;
	private Integer chunkNumber;
	private Integer totalChunks;
	private MultipartFile file;

	public String getSubscriberUniqueKey() {
		return subscriberUniqueKey;
	}

	public void setSubscriberUniqueKey(String subscriberUniqueKey) {
		this.subscriberUniqueKey = subscriberUniqueKey;
	}

	public String getProcessUniqueKey() {
		return processUniqueKey;
	}

	public void setProcessUniqueKey(String processUniqueKey) {
		this.processUniqueKey = processUniqueKey;
	}

	public String getFileUniqueKey() {
		return fileUniqueKey;
	}

	public void setFileUniqueKey(String fileUniqueKey) {
		this.fileUniqueKey = fileUniqueKey;
	}

	public Integer getChunkNumber() {
		return chunkNumber;
	}

	public void setChunkNumber(Integer chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	public Integer getTotalChunks() {
		return totalChunks;
	}

	public void setTotalChunks(Integer totalChunks) {
		this.totalChunks = totalChunks;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
