package com.data.integration.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.data.integration.data.FileChunk;

@Repository
public interface FileChunkRepository extends CrudRepository<FileChunk, Long> {

	Integer countByKey(String key);
	
	List<FileChunk> findByKeyOrderByChunkNumber(String key);
}
