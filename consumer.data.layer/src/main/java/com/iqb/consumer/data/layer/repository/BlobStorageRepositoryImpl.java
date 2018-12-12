/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:09:54
 * @version V1.0
 */
package com.iqb.consumer.data.layer.repository;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Repository;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Repository
public class BlobStorageRepositoryImpl extends AbstractStorageRepositoryImpl implements StorageRepository {

    @Resource(name = "blobGridFsTemplate")
    private GridFsOperations blobOperations;

    @Override
    public String save(InputStream inputStream, String contentType, String filename) {
        return save(blobOperations, inputStream, contentType, filename);
    }

    @Override
    public void delete(String filename) {
        delete(blobOperations, filename);
    }

    @Override
    public GridFSDBFile getByFilename(String filename) {
        return getByFilename(blobOperations, filename);
    }

    @Override
    public List<GridFSDBFile> listFiles() {

        return listFiles(blobOperations);
    }

    @Override
    public GridFSDBFile get(String id) {
        return get(blobOperations, id);
    }

    @Override
    public boolean exists(String md5) {
        return exists(blobOperations, md5);
    }

}
