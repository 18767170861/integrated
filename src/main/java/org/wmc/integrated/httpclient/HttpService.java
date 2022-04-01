package org.wmc.integrated.httpclient;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class HttpService {
    @Autowired
    private CloseableHttpClient httpClient;

    @Autowired
    private RequestConfig requestConfig;

    /**
     * 发送httpGet请求
     *
     * @param url
     * @return
     */
    public String doGet(String url) {
        String result = null;
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 发送httpPost请求 Content-Type: application/x-www-form-urlencoded;charset=utf-8
     *
     * @return
     */
    public String doPost_form(String url, Map<String, Object> paramMap, String chartset) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        List<NameValuePair> paramList = new ArrayList<>();
        if (paramMap != null) {
            Set<String> keySet = paramMap.keySet();
            NameValuePair pair = null;
            for (String key : keySet) {
                pair = new BasicNameValuePair(key, paramMap.get(key).toString());
                paramList.add(pair);
            }
        }
        UrlEncodedFormEntity entity = null;
        CloseableHttpResponse response = null;
        try {
            entity = new UrlEncodedFormEntity(paramList, chartset);
            httpPost.setEntity(entity);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), chartset);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }

    /**
     * 发送httpPost请求 Content-Type: application/json;charset=utf-8
     *
     * @return
     */
    public String doPost_json(String url, String jsonParam) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramList = new ArrayList<>();
        CloseableHttpResponse response = null;
        try {
            if (StringUtils.isNotBlank(jsonParam)) {
                httpPost.setEntity(new StringEntity(jsonParam, "utf-8"));
            }
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public String yuqing_Post_json(String url, String jsonParam, String authorization) {
        String result = null;
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramList = new ArrayList<>();
        CloseableHttpResponse response = null;
        try {
            if (StringUtils.isNotBlank(jsonParam)) {
                httpPost.setEntity(new StringEntity(jsonParam, "utf-8"));
            }
            httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
            httpPost.addHeader("authorization", authorization);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
}
