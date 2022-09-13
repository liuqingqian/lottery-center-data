package com.hermes.lotdata.infrastructure.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtil2 {
    private static CloseableHttpClient mClient;
    private final static int TIMEOUT = 600000;
    private static NetworkListener sNetworkListener;

    static {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(5000);
        connManager.setDefaultMaxPerRoute(500);
        connManager.setValidateAfterInactivity(5000);
        ConnectionKeepAliveStrategy myStrategy = (response, context) -> 5 * 1000;
        mClient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(myStrategy).build();
    }


    public static void setNetworkListener(NetworkListener networkListener) {
        NetUtil2.sNetworkListener = networkListener;
    }

    public static Response getHeaders(String url, Map<String, String> headers) {
        Response res;
        HttpGet get = new HttpGet(url);
        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();
        get.setConfig(rc);
        get.addHeader("Connection", "Keep-Alive");
        get.addHeader("Accept-Encoding", "gzip,deflate");
        if (MapUtils.isNotEmpty(headers)) {
            headers.entrySet().forEach(header -> {
                get.addHeader(header.getKey(), header.getValue());
            });
        }
        res = send(get);
        return res;
    }

    public static Response get(String url, Map<String, Object> params) {
        return get(url, params, null, true);
    }

    public static Response get(String url, Map<String, Object> params, Map<String, String> headers) {
        return get(url, params, headers, true);
    }

    public static Response get(String url, Map<String, Object> params, Map<String, String> headers, boolean encode) {

        StringBuilder sb = new StringBuilder(url);
        if (url.contains("?")) {
            sb.append("&");
        } else {
            sb.append("?");
        }

        for (String key : params.keySet()) {

            Object val = params.get(key);

            if (val == null) {
                val = "";
            }
            if (encode) {
                val = encode(String.valueOf(val));
            }
            sb.append(key).append("=").append(val).append("&");
        }

        return getHeaders(sb.toString(), headers);
    }

    public static Response post(String url, Map<String, ?> params, Map<String, String> headers) {
        List<NameValuePair> nvps = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, String.valueOf(params.get(key))));
        }
        UrlEncodedFormEntity entity = null;
        try {
            entity = new UrlEncodedFormEntity(nvps, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return post(url, entity, headers);

    }

    public static Response postJson(String url, Map<String, Object> params, Map<String, String> headers) {
        HttpPost post = new HttpPost(url);

        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();


        HttpEntity entity = null;
        try {
            entity = new ByteArrayEntity(JSON.toJSONString(params).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        post.addHeader("Content-Type", "application/json");

        if (MapUtils.isNotEmpty(headers)) {
            headers.entrySet().forEach(header -> {
                post.addHeader(header.getKey(), header.getValue());
            });
        }
        post.setEntity(entity);


        return send(post);

    }

    public static Response post(String url, HttpEntity entity, Map<String, String> headers) {
        HttpPost post = new HttpPost(url);

        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();

        post.setConfig(rc);
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Accept-Encoding", "gzip,deflate");

        post.setEntity(entity);

        if (MapUtils.isNotEmpty(headers)) {
            headers.entrySet().forEach(header -> {
                post.addHeader(header.getKey(), header.getValue());
            });
        }

//        post.addHeader("Cookie", cookies);
//        post.addHeader("User-Agent", ua);

        return send(post);
    }

    public static Response postXml(String url, HttpEntity entity, String ua, String cookies) {
        HttpPost post = new HttpPost(url);

        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();

        post.setConfig(rc);
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Accept-Encoding", "gzip,deflate");
        post.addHeader("Content-Type", "application/xml");

        post.setEntity(entity);

        post.addHeader("Cookie", cookies);
        post.addHeader("User-Agent", ua);

        return send(post);
    }

    public static Response postJson(String url, JSONObject param) {
        HttpPost post = new HttpPost(url);

        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();
        post.setConfig(rc);
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Accept-Encoding", "gzip,deflate");
        post.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        StringEntity entity = new StringEntity(param.toString(), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        return send(post);
    }

    public static Response postJsonArray(String url, JSONArray param) {
        HttpPost post = new HttpPost(url);

        RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT).build();
        post.setConfig(rc);
        post.addHeader("Connection", "Keep-Alive");
        post.addHeader("Accept-Encoding", "gzip,deflate");
        post.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        StringEntity entity = new StringEntity(param.toString(), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        return send(post);
    }

    public static Response send(HttpUriRequest request) {
        if (sNetworkListener != null && request != null) {
            Map<String, Object> headers = sNetworkListener.getHeader();
            for (Map.Entry<String, Object> header : headers.entrySet()) {
                request.addHeader(header.getKey(), String.valueOf(header.getValue()));
            }
        }
        Response res = new Response();
        CloseableHttpResponse httpRes = null;
        try {
            long start = System.currentTimeMillis();
            httpRes = mClient.execute(request);
            int code = httpRes.getStatusLine().getStatusCode();
            String str = EntityUtils.toString(httpRes.getEntity(), "UTF-8");
            long end = System.currentTimeMillis();


            res.code = code;
            res.rawContent = str;
            try {
                res.content = JSON.parseObject(str);
            } catch (JSONException e) {
            }
        } catch (Exception e) {
            res.code = -1;
        } finally {
            if (httpRes != null) {
                try {
                    httpRes.close();
                } catch (IOException e) {

                }
            }
        }

        return res;
    }

    public static String encode(String s) {
        String str = "";
        try {
            str = URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return str;
    }

    public static String decode(String s) {
        String str = "";
        try {
            str = URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return str;
    }

    /**
     * 只编码url中的中文汉字和符号
     */
    public static String encodeUrl(String url) {
        // 匹配中文字符的正则表达式： [\u4e00-\u9fa5]
        // 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
        Matcher m = Pattern.compile("[^\\x00-\\xff]").matcher(url);
        StringBuffer encodeUrl = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(encodeUrl, encode(m.group(0)));
        }
        m.appendTail(encodeUrl);
        return encodeUrl.toString();

    }

    public static String getCurrentHost(HttpServletRequest request) {
        String scheme = request.getScheme();
        int port = request.getServerPort();
        String name = request.getServerName();
        String host = scheme + "://" + name + (port == 80 ? "" : ":" + port);
        return host;
    }

    public static class Response {
        public static final int STATE_SUCC = 200;
        public int code;
        public String rawContent;
        public JSONObject content;
    }

    public static String readRequestStream(HttpServletRequest request) {
        StringBuilder out = new StringBuilder();
        if (request == null) {
            return out.toString();
        }
        byte[] b = new byte[4096];
        try {
            for (int n; (n = request.getInputStream().read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }


    public interface NetworkListener {
        Map<String, Object> getHeader();
    }
}
