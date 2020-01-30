package com.peter.solo.markdown.monitor;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.input.FileUpdate;
import com.peter.solo.markdown.input.InputService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对文件进行监听。
 */
@Slf4j
public class MarkdownAlterationListener implements FileAlterationListener {
    private AtomicInteger listenerCount;
    private InputService inputService;
    private FileUpdate fileUpdate;

    public MarkdownAlterationListener(InputService inputService, FileUpdate fileUpdate, final AtomicInteger listenerCount) {
        this.inputService = inputService;
        this.fileUpdate = fileUpdate;
        this.listenerCount = listenerCount;
    }

    @Override
    public void onStart(FileAlterationObserver observer) {
        listenerCount.incrementAndGet();
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
        listenerCount.decrementAndGet();

        try {
            observer.initialize();
        }
        catch (Exception e) {
            log.error("observer对象重新初始化失败：", e);
        }

        log.debug(String.format("listener stop"));
    }

    private void upfile(File file) throws IOException {
        JSONObject ret = inputService.updateFile(file);

        if(null == ret) {
            log.error("文件上传失败，返回结果为空。File: " + file.getCanonicalPath());
            return;
        }

        if(0 != ret.getInteger("code")) {
            log.error("文件上传失败：" + ret.getString("msg"));
            return;
        }

        JSONObject retData = ret.getJSONObject("data");
        String id = null == retData ? null : retData.getString("id");

        fileUpdate.update(id, file);
    }
}
