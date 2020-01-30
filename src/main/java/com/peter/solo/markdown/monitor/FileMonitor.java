package com.peter.solo.markdown.monitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 监控文件系统
 */
@Slf4j
@Component
public class FileMonitor {
    private FileAlterationMonitor monitor;

    public FileMonitor(@Value("${solo.blog.refresh.interval}") Integer interval,
                       @Autowired MarkdownAlterationObserver observer, @Autowired MarkdownAlterationListener listener) {
        monitor = new FileAlterationMonitor(interval);
        observer.addListener(listener);
        monitor.addObserver(observer);
    }

    public void start() throws Exception {
        monitor.start();
    }

    public void stop() throws Exception {
        monitor.stop();
    }

}
