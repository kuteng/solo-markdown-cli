package com.peter.solo.markdown.input;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过模拟HTTP请求，上传文件以更新博客内容。
 */
@Data
@Slf4j
@Component
public class InputService {
    @Value("${solo.input.url}")
    private String inputUrl;


    public JSONObject updateFile(File file) throws IOException {
        if(null == file || !file.exists()) {
            log.warn(String.format("文件不存在：", null == file ? "null" : file.getPath()));
            return null;
        }

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(inputUrl);
        // 使用InputStreamEntity更加通用，来演示从任意源输出数据的能力
        // InputStreamEntity entity = new InputStreamEntity(new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
        // entity.setChunked(true);
        // 在这个特定的实例中使用FileEntity类可能更合适，但是我们
        // FileEntity entity = new FileEntity(file, ContentType.MULTIPART_FORM_DATA);
        // httpPost.setEntity(entity);
        // 上传文件
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.addBinaryBody("file", file);
        HttpEntity httpEntity = multipartEntityBuilder.build();
        httpPost.setEntity(httpEntity);
        CloseableHttpResponse response1 = httpclient.execute(httpPost);

        try {
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            // EntityUtils.consume(entity1);
            String content = EntityUtils.toString(entity1, "utf-8");
            return JSONObject.parseObject(content);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            response1.close();
        }
    }

    private void post() throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(inputUrl);
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("username", "vip"));
        nvps.add(new BasicNameValuePair("password", "secret"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response1 = httpclient.execute(httpPost);

        try {
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            // EntityUtils.consume(entity1);
            String content = EntityUtils.toString(entity1, "utf-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            response1.close();
        }
    }

    public static void main(String[] args) {
        InputService inputService = new InputService();
        inputService.setInputUrl("http://localhost:8080/plugin/markdown/input");
        File file = new File("/Users/yandong/Blogs/solo/note1.md");
        try {
            inputService.updateFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
