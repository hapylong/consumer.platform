/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:28:01
 * @version V1.0
 */
package com.iqb.consumer.service.layer.storage;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.iqb.consumer.data.layer.repository.StorageRepository;
import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
@Service
public class PackageStorageServiceImpl extends AbstractStorageServiceImpl implements StorageService {

    @Resource(name = "packageStorageRepositoryImpl")
    private StorageRepository packageStorageRepository;

    @Override
    public String save(InputStream inputStream, String contentType, String filename) {
        return save(packageStorageRepository, inputStream, contentType, filename);
    }

    @Override
    public void delete(String filename) {
        delete(packageStorageRepository, filename);
    }

    @Override
    public GridFSDBFile getByFilename(String filename) {
        return getByFilename(packageStorageRepository, filename);
    }

    @Override
    public byte[] getFileContentByFilename(String filename) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<GridFSDBFile> listFiles() {
        return listFiles(packageStorageRepository);
    }

    @Override
    public GridFSDBFile get(String id) {
        return get(packageStorageRepository, id);
    }

    @Override
    public boolean exists(String md5) {
        return exists(packageStorageRepository, md5);
    }

}
