package com.peter.solo.markdown.monitor;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.entity.BlogFile;
import com.peter.solo.markdown.input.FileUpdate;
import com.peter.solo.markdown.input.InputService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * 对文件进行监听。
 */
@Slf4j
@Component
public class MarkdownAlterationListener implements FileAlterationListener {
    @Autowired
    private InputService inputService;
    @Autowired
    private FileUpdate fileUpdate;

    @Override
    public void onStart(FileAlterationObserver observer) {
        log.debug("listener start");
    }

    @Override
    public void onDirectoryCreate(File directory) {
        try {
            log.info(String.format("folder: %s is created", directory.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectoryChange(File directory) {
        try {
            log.info(String.format("folder: %s is changed", directory.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectoryDelete(File directory) {
        try {
            log.info(String.format("folder: %s is deleted", directory.getCanonicalPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileCreate(File file) {
        try {
            log.info(String.format("file: %s is created", file.getCanonicalPath()));
            upfile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileChange(File file) {
        try {
            log.info(String.format("file: %s is changed", file.getCanonicalPath()));
            upfile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFileDelete(File file) {
        try {
            log.info(String.format("file: %s is deleted", file.getCanonicalPath()));
            // TODO by yandong: 文件时应该做更细节的事情。
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop(FileAlterationObserver observer) {
        // 重新初始化observer对象，免得刚刚update的对象又被input-blog.
        try {
            observer.initialize();
        }
        catch (Exception e) {
            log.error("observer对象重新初始化失败：", e);
        }

        log.debug(String.format("listener stop"));
    }

    private void upfile(File file) throws IOException {
        BlogFile blogFile = new BlogFile(file);

        if(null == blogFile.getMetaInfo() || !blogFile.getMetaInfo().isBlog()) {
            log.info("文件({})不是博客文章，不进行上传。", file.getCanonicalPath());
            return;
        }

        JSONObject ret = inputService.updateFile(blogFile);

        if(null == ret) {
            log.error("文件上传失败，返回结果为空。File: " + file.getCanonicalPath());
            return;
        }

        log.debug("返回结果是：{}", ret.toJSONString());

        if(0 != ret.getInteger("code")) {
            log.error("文件上传失败：" + ret.getString("msg"));
            return;
        }

        JSONObject retData = ret.getJSONObject("data");
        String id = null == retData ? null : retData.getString("id");

        fileUpdate.update(id, blogFile);
    }
}
