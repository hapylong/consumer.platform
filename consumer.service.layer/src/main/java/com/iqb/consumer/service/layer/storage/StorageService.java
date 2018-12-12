/**
 * @Copyright (c) http://www.iqianbang.com/ All rights reserved.
 * @Description: TODO
 * @date 2016年9月2日 下午3:17:15
 * @version V1.0
 */
package com.iqb.consumer.service.layer.storage;

import java.io.InputStream;
import java.util.List;

import com.mongodb.gridfs.GridFSDBFile;

/**
 * @author <a href="zhuyaoming@aliyun.com">yeoman</a>
 */
public interface StorageService {
    /**
     * 将指定的内容类型的数据流保存指定的文件中.
     * 
     * @param inputStream
     * @param contentType
     * @param filename
     * @return the Id of the file
     */
    String save(InputStream inputStream, String contentType, String filename);

    /**
     * 删除给定的文件.
     * 
     * @param filename
     */
    void delete(String filename);

    /**
     * 读取给定的文件.
     * 
     * @param filename
     * @return
     */
    GridFSDBFile getByFilename(String filename);

    /**
     * 读取给定的文件内容.
     * 
     * @param filename
     * @return
     */
    byte[] getFileContentByFilename(String filename);

    /**
     * 返回所有的文件列表.
     * 
     * @return
     */
    List<GridFSDBFile> listFiles();

    /**
     * 按文件对象的 id 查找文件.
     * 
     * @param id
     * @return
     */
    GridFSDBFile get(String id);

    /**
     * check file exists by md5
     * 
     * @param md5
     * @return
     */
    boolean exists(String md5);
}
