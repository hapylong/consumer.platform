package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author jack
 * 
 */
public class ProFileUtil {

    /**
     * 获取文件内容
     * 
     * @param pro
     * @return
     * @throws Exception
     */
    public static String getFileStr(String filepath) throws Exception {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
            String readLine = null;

            while ((readLine = br.readLine()) != null) {
                if (readLine.startsWith("--")) {
                    continue;
                } else {
                    sb.append(readLine);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException e) {
            throw new Exception("私钥输入流为空");
        } finally {
            if (null != br)
                br.close();
        }
        return sb.toString();
    }

    /**
     * 
     * @param pro
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public static byte[] getFileByte(String filepath) throws IOException {
        byte b[];
        InputStream in = new FileInputStream(new File(filepath));
        if (null == in)
            throw new RuntimeException((new StringBuilder()).append("文件不存在").append(filepath).toString());
        b = new byte[20480];
        in.read(b);
        if (null != in)
            in.close();
        return b;
    }
}
