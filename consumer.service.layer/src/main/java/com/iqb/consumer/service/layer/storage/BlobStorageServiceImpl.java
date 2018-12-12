/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:24:21
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
public class BlobStorageServiceImpl extends AbstractStorageServiceImpl implements StorageService {

    @Resource(name = "blobStorageRepositoryImpl")
    private StorageRepository blobStorageRepository;

    @Override
    public String save(InputStream inputStream, String contentType, String filename) {
        return save(blobStorageRepository, inputStream, contentType, filename);
    }

    @Override
    public void delete(String filename) {
        delete(blobStorageRepository, filename);
    }

    @Override
    public GridFSDBFile getByFilename(String filename) {
        return getByFilename(blobStorageRepository, filename);
    }

    @Override
    public byte[] getFileContentByFilename(String filename) {
        byte[] content = getFileContentByFilename(blobStorageRepository, filename);
        return content;
    }

    @Override
    public List<GridFSDBFile> listFiles() {
        return listFiles(blobStorageRepository);
    }

    @Override
    public GridFSDBFile get(String id) {
        return get(blobStorageRepository, id);
    }

    @Override
    public boolean exists(String md5) {
        return exists(blobStorageRepository, md5);
    }

}
