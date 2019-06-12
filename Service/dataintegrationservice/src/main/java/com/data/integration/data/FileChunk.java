package com.data.integration.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FileChunk")
public class FileChunk {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "FileChunkID", unique = true, nullable = false)
	private long fileChunkID;

	@Column(name = "FileUniqueKey",nullable = false)
	private String key;

	@Column(name = "InputFileChunkPath", unique = true, nullable = false)
	private String inputFileChunkPath;

	@Column(name = "ChunkNumber")
	private Integer chunkNumber;

	@Column(name = "TotalChunks")
	private Integer totalChunks;

	@Column(name = "ReceivedTime")
	private Date receivedTime;

	public FileChunk() {
		// TODO Auto-generated constructor stub
	}

	public FileChunk(long fileChunkID, String key, String inputFileChunkPath,
			Integer chunkNumber, Integer totalChunks, Date receivedTime) {
		super();
		this.fileChunkID = fileChunkID;
		this.key = key;
		this.inputFileChunkPath = inputFileChunkPath;
		this.chunkNumber = chunkNumber;
		this.totalChunks = totalChunks;
		this.receivedTime = receivedTime;
	}

	public long getFileChunkID() {
		return fileChunkID;
	}

	public void setFileChunkID(long fileChunkID) {
		this.fileChunkID = fileChunkID;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getInputFileChunkPath() {
		return inputFileChunkPath;
	}

	public void setInputFileChunkPath(String inputFileChunkPath) {
		this.inputFileChunkPath = inputFileChunkPath;
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

	public Date getReceivedTime() {
		return receivedTime;
	}

	public void setReceivedTime(Date receivedTime) {
		this.receivedTime = receivedTime;
	}
}
