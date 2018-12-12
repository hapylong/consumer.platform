package com.iqb.consumer.service.layer.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONObject;
import com.iqb.consumer.data.layer.bean.image.ImageBean;
import com.iqb.consumer.data.layer.biz.image.ImageBiz;
import com.iqb.etep.common.base.config.BaseConfig;
import com.iqb.etep.common.utils.JSONUtil;

@Component
public class ImageServiceImpl implements ImageService {

    /** 日志处理 **/
    private static Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageBiz imageBiz;
    @Resource(name = "baseConfig")
    private BaseConfig config;

    @Override
    public int batchInsertImage(JSONObject objs) {
        return imageBiz.batchInsertImage(objs);
    }

    @Override
    public int insertImage(JSONObject objs) {
        ImageBean imageBean = JSONUtil.toJavaObject(objs, ImageBean.class);
        return imageBiz.insertImage(imageBean);
    }

    @Override
    public List<ImageBean> selectList(JSONObject objs) {
        return imageBiz.selectList(objs);
    }

    @Override
    public int deleteImageByPath(JSONObject objs) {
        String imgPath = objs.getString("imgPath");
        return imageBiz.deleteImageByPath(imgPath);
    }

    /**
     * 将doc文档转换为html
     * 
     * @param params
     * @return
     * @throws Exception
     * @Author haojinlong Create Date: 2018年7月24日
     */
    @Override
    public String convertDocToHtml(JSONObject objs) throws Exception {
        String returnFilePath = "";
        ImageBean imageBean = imageBiz.selectImageInfo(objs);
        if (imageBean != null) {
            // 文件存储base路径
            String realPath = config.getCommon_basedir();
            // html文件存放路径
            String putPath = realPath.substring(0, realPath.lastIndexOf("/")) + "/etep.web/view/tempdoc/";
            String fileName = imageBean.getImgName();
            fileName = fileName.substring(0, fileName.lastIndexOf(".doc"));
            // 校验文件是否存在
            File file = new File(putPath, fileName + ".html");
            logger.info("---将doc文档转换为html---putPath--{}---fileName--{}---file---{}", putPath, fileName, file.exists());
            if (!file.exists()) {
                InputStream input = new FileInputStream(realPath + imageBean.getImgPath());
                HWPFDocument wordDocument = new HWPFDocument(input);
                // 创建doc文档转html转化器
                WordToHtmlConverter wordToHtmlConverter =
                        new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                    @Override
                    public String savePicture(byte[] content, PictureType pictureType, String suggestedName,
                            float widthInches,
                            float heightInches) {
                        return suggestedName;
                    }
                });
                // 装载word文件
                wordToHtmlConverter.processDocument(wordDocument);
                List<Picture> pics = wordDocument.getPicturesTable().getAllPictures();
                if (pics != null) {
                    for (int i = 0; i < pics.size(); i++) {
                        Picture pic = (Picture) pics.get(i);
                        try {
                            pic.writeImageContent(new FileOutputStream(realPath + pic.suggestFullFileName()));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Document htmlDocument = wordToHtmlConverter.getDocument();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                DOMSource domSource = new DOMSource(htmlDocument);
                StreamResult streamResult = new StreamResult(outStream);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
                outStream.close();
                String content = new String(outStream.toByteArray());

                FileUtils.writeStringToFile(new File(putPath, fileName + ".html"), content, "utf-8");
            }
            returnFilePath = "/etep.web/view/tempdoc/" + fileName + ".html";
        }
        return returnFilePath;
    }

    @Override
    public String downLoadImage(JSONObject objs, HttpServletResponse response)
            throws Exception {

        ImageBean img = imageBiz.selectImageInfo(objs);
        if (img.getImgPath() == null || img.getImgPath().trim().equals("")) {
            logger.error("ImageBean.ImgPath is null");
            return "fail";
        }
        if (img.getImgName() == null || img.getImgName().trim().equals("")) {
            logger.error("ImageBean.ImgName is null");
            return "fail";
        }

        String fileRealPath = config.getCommon_basedir() + img.getImgPath();
        String filedisplay = new String(img.getImgName().getBytes(), "ISO-8859-1");
        // DateUtil.parseYYYYMMDD(new Date())+CommonUtil.randomString(6)
        // + (fileName.lastIndexOf(".") == -1 ?"":fileName.substring(fileName.lastIndexOf(".")));

        response.setContentType("applicaiton/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);

        InputStream is = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            is = new FileInputStream(new File(fileRealPath));
            bis = new BufferedInputStream(is);
            os = response.getOutputStream();
            bos = new BufferedOutputStream(os);

            byte[] b = new byte[1024];
            int len = 0;
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bos.flush();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("", e);
            return "fail";
        } finally {
            if (bis != null)
                bis.close();
            if (is != null)
                is.close();
            if (bos != null)
                bos.close();
            if (os != null)
                os.close();
        }

    }

}
