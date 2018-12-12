package util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

/*
 * 利用HttpClient进行post请求的工具类
 */
public class HttpsClientUtil {

    private static class SingletonHolder {
        private static final HttpsClientUtil INSTANCE = new HttpsClientUtil();
    }

    /**
     * Singleton! This class can be created only once!
     * 
     * @return
     */
    public static final HttpsClientUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public String doPostold(String url, String jsonStr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(URLEncoder.encode(jsonStr, charset));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    // result = URLDecoder.decode(EntityUtils.toString(resEntity,charset));
                    result = EntityUtils.toString(resEntity, "ISO8859-1");
                    result = java.net.URLDecoder.decode(result, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    public String doPost(String url, String jsonStr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient(); // 一般用法
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(URLEncoder.encode(jsonStr, charset));
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
     * 
     * @param url
     * @param map
     * @param charset
     * @return
     */
    @SuppressWarnings("deprecation")
    public String doPost2(String url, String jsonStr, String charset) {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new SSLClient();
            httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(URLEncoder.encode(jsonStr, charset));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = URLDecoder.decode(EntityUtils.toString(resEntity, charset));
                    // result=EntityUtils.toString(resEntity,"ISO8859-1");
                    result = java.net.URLDecoder.decode(result, charset);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
