package com.peter.solo.markdown.monitor;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.input.FileUpdate;
import com.peter.solo.markdown.input.InputService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控文件系统
 */
@Slf4j
@Service
public class FileMonitor {
    @Autowired
    private InputService inputService;
    @Autowired
    private FileUpdate fileUpdate;
    @Value("${solo.blog.root}")
    private String blogRoot;

    private FileAlterationMonitor monitor;

    private final AtomicInteger listenerCount = new AtomicInteger(0);

    public void init() {
        monitor = new FileAlterationMonitor();
        MarkdownAlterationObserver observer = new MarkdownAlterationObserver(blogRoot);
        observer.addListener(new MarkdownAlterationListener(inputService, fileUpdate, listenerCount));
        monitor.addObserver(observer);
    }

    public void start() throws Exception {
        monitor.start();
    }

    public void stop() throws Exception {
        monitor.stop();
    }

}
