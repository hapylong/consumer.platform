/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:11:18
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
public class PackageStorageRepositoryImpl extends AbstractStorageRepositoryImpl implements StorageRepository {

    @Resource(name = "pkgGridFsTemplate")
    private GridFsOperations pkgOperations;

    @Override
    public String save(InputStream inputStream, String contentType, String filename) {
        return save(pkgOperations, inputStream, contentType, filename);
    }

    @Override
    public void delete(String filename) {
        delete(pkgOperations, filename);
    }

    @Override
    public GridFSDBFile getByFilename(String filename) {
        return getByFilename(pkgOperations, filename);
    }

    @Override
    public List<GridFSDBFile> listFiles() {

        return listFiles(pkgOperations);
    }

    @Override
    public GridFSDBFile get(String id) {
        return get(pkgOperations, id);
    }

    @Override
    public boolean exists(String md5) {
        return exists(pkgOperations, md5);
    }

}
