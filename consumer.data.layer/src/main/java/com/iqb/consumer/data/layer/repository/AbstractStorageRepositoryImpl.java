/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:08:20
 * @version V1.0
 */
package com.iqb.consumer.data.layer.repository;

import java.io.InputStream;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public abstract class AbstractStorageRepositoryImpl {

    private static final Logger logger = LoggerFactory.getLogger(AbstractStorageRepositoryImpl.class);

    /**
     * 将指定的内容类型的数据流保存指定的文件中.
     * 
     * @param gridFsOperations
     * @param inputStream
     * @param contentType
     * @param filename
     * @return the Id of the file
     */
    public String save(GridFsOperations gridFsOperations, InputStream inputStream, String contentType, String filename) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }
        if (contentType == null) {
            throw new IllegalArgumentException("contentType is null");
        }
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        GridFSDBFile file = getByFilename(gridFsOperations, filename);
        if (file == null) {
            logger.debug("save to file: {}", filename);

            DBObject metaData = new BasicDBObject();
            metaData.put("meta1", filename);

            GridFSFile newFile = gridFsOperations.store(inputStream, filename, contentType, metaData);

            return newFile.getId().toString();
        } else {
            logger.info("file already existing: {}, return the existing one", filename);
            return file.getId().toString();
        }
    }

    /**
     * 将指定的内容类型的数据流保存指定的文件中，如果该文件已经存在，覆盖原文件.
     * 
     * @param gridFsOperations
     * @param inputStream
     * @param contentType
     * @param filename
     * @return the Id of the file
     */
    public String saveOrUpdate(GridFsOperations gridFsOperations, InputStream inputStream, String contentType,
            String filename) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }
        if (contentType == null) {
            throw new IllegalArgumentException("contentType is null");
        }
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        logger.debug("saveOrUpdate to file: {}", filename);
        DBObject metaData = new BasicDBObject();
        metaData.put("meta1", filename);

        GridFSFile newFile = gridFsOperations.store(inputStream, filename, contentType, metaData);

        return newFile.getId().toString();
    }

    /**
     * 删除给定的文件.
     * 
     * @param gridFsOperations
     * @param filename
     */
    public void delete(GridFsOperations gridFsOperations, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        logger.info("delete file: {}", filename);

        gridFsOperations.delete(new Query(Criteria.where("filename").is(filename)));
    }

    /**
     * 读取给定的文件.
     * 
     * @param gridFsOperations
     * @param filename
     * @return
     */
    public GridFSDBFile getByFilename(GridFsOperations gridFsOperations, String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        logger.debug("Finding by filename: {}", filename);
        List<GridFSDBFile> files = gridFsOperations.find(new Query(Criteria.where("filename").is(filename)));
        if (files != null && files.isEmpty() == false) {
            return files.get(0); // return the first one in the case of duplicated
        } else {
            return null;
        }
    }

    /**
     * 返回所有的文件列表.
     * 
     * @param gridFsOperations
     * @return
     */
    public List<GridFSDBFile> listFiles(GridFsOperations gridFsOperations) {

        return gridFsOperations.find(null);
    }

    /**
     * 按文件对象的 id 查找文件.
     * 
     * @param gridFsOperations
     * @param id
     * @return
     */
    public GridFSDBFile get(GridFsOperations gridFsOperations, String id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        logger.debug("Finding by ID: {}", id);
        return gridFsOperations.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
    }

    public boolean exists(GridFsOperations gridFsOperations, String md5) {
        GridFSDBFile file = gridFsOperations.findOne(new Query(Criteria.where("md5").is(md5)));
        return null != file;
    }
}
