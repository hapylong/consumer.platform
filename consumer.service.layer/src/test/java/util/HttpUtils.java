/**
 * Copyright: Copyright (c)2014 Company: UCFGROUP
 */
package util;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * @author：PTU
 * @since：2014年5月14日 下午4:00:17
 * @version:1.0
 * 
 *              发送HTTP请求的工具类
 */
public final class HttpUtils {

    private static Logger log = Logger.getLogger(HttpUtils.class);

    private static final int HTTP_CON_TIME_OUT = 5000;
    private static final int HTTP_SO_TIME_OUT = 5000;
    private static final int HTTP_MAX_CONNECTIONS = 1000;
    private static final int MAX_RETRY_TIMES = 3;

    private static CloseableHttpClient httpClient;

    private HttpUtils() {

    }

    static {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            TrustManager trustManager = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {}

                public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                        throws CertificateException {}
            };
            sc.init(null, new TrustManager[] {trustManager}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    // http注册
                    .register("http", PlainConnectionSocketFactory.INSTANCE)
                    // https注册
                    .register("https", new SSLConnectionSocketFactory(sc)).build();

            PoolingHttpClientConnectionManager connectionManager =
                    new PoolingHttpClientConnectionManager(socketFactoryRegistry);

            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CON_TIME_OUT)
                    // 设置请求的最大响应时间
                    .setConnectionRequestTimeout(HTTP_CON_TIME_OUT)
                    // 设置TCP建立连接的超时时间
                    .setSocketTimeout(HTTP_SO_TIME_OUT).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();

            SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

            connectionManager.setDefaultSocketConfig(socketConfig);
            // Create message constraints
            MessageConstraints constraints =
                    MessageConstraints.custom().setMaxHeaderCount(200).setMaxLineLength(100000).build();

            // Create connection configuration
            ConnectionConfig connectionConfig =
                    ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                            .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
                            .setMessageConstraints(constraints).build();

            connectionManager.setDefaultConnectionConfig(connectionConfig);
            // 连接池里的最大连接数
            connectionManager.setMaxTotal(HTTP_MAX_CONNECTIONS);
            // 每个路由的默认最大连接数,这服务器的数量以及连接池的最大连接数有关
            connectionManager.setDefaultMaxPerRoute(HTTP_MAX_CONNECTIONS / 2);

            List<BasicHeader> defaultHeaders = new ArrayList<BasicHeader>() {
                private static final long serialVersionUID = 1263811764541797122L;
                {
                    // 不使用缓存
                    add(new BasicHeader("Expires", "0"));
                    add(new BasicHeader("Cache-Control", "no-store"));
                }
            };

            httpClient =
                    HttpClientBuilder.create()
                            .setRetryHandler(new DefaultHttpRequestRetryHandler(MAX_RETRY_TIMES, true))
                            .setConnectionManager(connectionManager).setDefaultRequestConfig(requestConfig)
                            .setDefaultHeaders(defaultHeaders).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送HTTP GET 请求并并将返回值以字符串的形式返回，默认以UTF-8的编码格式返回
     * 
     * @param url 请求地址
     * @param queryParameterPair 查询参数
     * @param encoding 期望的返回的字符编码格式
     * @return
     */
    public static String sendGetRequest(String url, Map<String, String> queryParameterPair, String encoding) {
        CloseableHttpResponse response = null;

        try {

            URIBuilder UriBuilder = new URIBuilder(url);

            if (queryParameterPair != null && !queryParameterPair.isEmpty()) {
                for (Map.Entry<String, String> param : queryParameterPair.entrySet()) {
                    UriBuilder.addParameter(param.getKey(), param.getValue());
                }
            }
            HttpGet httpGet = new HttpGet(UriBuilder.build());
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");

            response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    if (null == encoding || "".equals(encoding)) {
                        encoding = "UTF-8";
                    }
                    String result = EntityUtils.toString(entity, encoding);
                    // 这个方法也可以把底层的流给关闭了
                    EntityUtils.consume(entity);
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送请求失败！", e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    log.error("关闭连接失败！", e);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public static String convertMapToParams(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder params = new StringBuilder();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = map.get(key);
            params.append(key);
            params.append("=");
            params.append(java.net.URLEncoder.encode(value));
            params.append("&");
        }
        String result = "";
        if (params.toString().endsWith("&")) {
            result = params.substring(0, params.length() - 1);
        }
        return result;
    }

    public static void main(String[] args) {}
}
