package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public final class HttpClientUtil {

    private static Logger logger = Logger.getLogger(HttpClientUtil.class);
    private static final String CONTENT_ENCODING = "UTF-8";
    public static final Charset GBK = Charset.forName(CONTENT_ENCODING);
    private static final String CONTENT_TYPE = "application/json";
    public static final int CONNECTION_TIMEOUT_MS = 360000;
    public static final int SO_TIMEOUT_MS = 360000;

    /******************************************* HTTP POST *******************************************************/
    /**
     * POST
     * 
     * @param url
     * @param jsonString
     * @return
     * @throws Exception
     */
    public static String httpPost(String url, String jsonString)
            throws Exception {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;
        try {
            HttpClient client = buildHttpClient(true);
            HttpPost post = new HttpPost(url);
            post.setConfig(buildRequestConfig());
            StringEntity entity = new StringEntity(jsonString, CONTENT_ENCODING);
            entity.setContentEncoding(CONTENT_ENCODING);
            entity.setContentType(CONTENT_TYPE);
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();
            reader = new BufferedReader(new InputStreamReader(
                    httpEntity.getContent()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (Exception e) {
            logger.error("POST", e);
        }

        finally {
            if (reader != null) {
                reader.close();
            }
        }
        return builder.toString();
    }

    /**
     * 
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpPost(String url, Map<String, String> params)
            throws URISyntaxException, ClientProtocolException, IOException {
        return httpPost(url, params, CONTENT_ENCODING);
    }

    /**
     * 
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String httpPost(String url, Map<String, String> params, String charset)
            throws URISyntaxException, ClientProtocolException, IOException {

        HttpClient client = buildHttpClient(false);

        HttpPost postMethod = buildHttpPost(url, params);

        HttpResponse response = client.execute(postMethod);

        assertStatus(response);

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, charset);
            return returnStr;
        }

        return null;
    }

    /**
     * 
     * @param res
     * @throws HttpException
     */
    static void assertStatus(HttpResponse res) throws IOException {
        switch (res.getStatusLine().getStatusCode()) {
            case HttpStatus.SC_OK:
                logger.info("Connection OK....");
                break;
            default:
                throw new IOException("��������Ӧ״̬�쳣,ʧ��...");
        }
    }

    /**
     * 
     * @return
     */
    public static RequestConfig buildRequestConfig() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SO_TIMEOUT_MS)
                .setConnectTimeout(CONNECTION_TIMEOUT_MS).build();
        return requestConfig;
    }

    /**
     * 
     * @param isMultiThread
     * @return
     */
    public static HttpClient buildHttpClient(boolean isMultiThread) {

        CloseableHttpClient client;

        if (isMultiThread)
            client = HttpClientBuilder
                    .create()
                    .setConnectionManager(
                            new PoolingHttpClientConnectionManager()).build();
        else
            client = HttpClientBuilder.create().build();
        return client;
    }

    /**
     * 
     * @param url
     * @param headers
     * @return
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    public static HttpPost buildHttpPost(String url, Map<String, String> params)
            throws UnsupportedEncodingException, URISyntaxException {
        HttpPost post = new HttpPost(url);
        setCommonHttpMethod(post);
        HttpEntity he = null;
        if (params != null) {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            }
            he = new UrlEncodedFormEntity(formparams, GBK);
            post.setEntity(he);
        }
        // setContentLength(post, he);
        return post;

    }

    public static void setCommonHttpMethod(HttpRequestBase httpMethod) {
        httpMethod.setHeader(HTTP.CONTENT_ENCODING, CONTENT_ENCODING);
    }

    /******************************************* HTTP GET *******************************************************/

    /**
     * GEET
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public static String httpGet(String url) throws Exception {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet();
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        HttpEntity httpEntity = response.getEntity();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpEntity.getContent()));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return builder.toString();
    }
}
