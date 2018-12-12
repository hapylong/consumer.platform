/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:17:52
 * @version V1.0
 */
package com.iqb.consumer.service.layer.storage;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iqb.consumer.common.exception.EntityNotFoundException;
import com.iqb.consumer.data.layer.repository.StorageRepository;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class AbstractStorageServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(AbstractStorageServiceImpl.class);

    /**
     * 将指定的内容类型的数据流保存指定的文件中.
     * 
     * @param storageRepository
     * @param inputStream
     * @param contentType
     * @param filename
     * @return the Id of the file
     */
    public String save(StorageRepository storageRepository, InputStream inputStream, String contentType, String filename) {
        return storageRepository.save(inputStream, contentType, filename);
    }

    /**
     * 删除给定的文件.
     * 
     * @param storageRepository
     * @param filename
     */
    public void delete(StorageRepository storageRepository, String filename) {
        storageRepository.delete(filename);
    }

    /**
     * 读取给定的文件.
     * 
     * @param storageRepository
     * @param filename
     * @return
     */
    public GridFSDBFile getByFilename(StorageRepository storageRepository, String filename) {
        return storageRepository.getByFilename(filename);
    }

    /**
     * 读取给定的文件.
     * 
     * @param storageRepository
     * @param filename
     * @return
     */
    public byte[] getFileContentByFilename(StorageRepository storageRepository, String filename) {
        GridFSDBFile file = storageRepository.getByFilename(filename);

        if (file == null) {
            throw new EntityNotFoundException(filename, "The file not found for name: " + filename);
        }

        try (InputStream in = file.getInputStream()) {
            byte[] data = IOUtils.toByteArray(in);
            return data;
        } catch (Exception e) {
            logger.error("Exception while retrieve the image: {}", filename, e);
            throw new EntityNotFoundException(filename, "The file not found", e);
        }
    }

    /**
     * 返回所有的文件列表.
     * 
     * @param storageRepository
     * @return
     */
    public List<GridFSDBFile> listFiles(StorageRepository storageRepository) {
        return storageRepository.listFiles();
    }

    /**
     * 按文件对象的 id 查找文件.
     * 
     * @param storageRepository
     * @param id
     * @return
     */
    public GridFSDBFile get(StorageRepository storageRepository, String id) {
        return storageRepository.get(id);
    }

    public boolean exists(StorageRepository storageRepository, String md5) {
        return storageRepository.exists(md5);
    }
}
