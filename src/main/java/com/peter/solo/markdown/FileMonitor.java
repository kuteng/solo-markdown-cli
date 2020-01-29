package com.peter.solo.markdown;

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
public class FileMonitor {
    @Autowired
    private InputService inputService;
    private FileAlterationMonitor monitor;

    private final AtomicInteger listenerCount = new AtomicInteger(0);

    public FileMonitor(String blogRoot) {
        monitor = new FileAlterationMonitor();
        FileAlterationObserver observer = new FileAlterationObserver(blogRoot, new MarkdownFileFilter());
        observer.addListener(new MarkdownAlterationListener(listenerCount));
        monitor.addObserver(observer);
    }

    public void start() throws Exception {
        monitor.start();
    }

    public void stop() throws Exception {
        monitor.stop();
    }

    /**
     * 文件过滤<br/>
     * 只有文件夹、不是以"."开头的".md"文件才能通过过滤。
     */
    @Slf4j
    public static class MarkdownFileFilter implements FileFilter {
        @Override
        public boolean accept(File file) {
            String fileName = FilenameUtils.getName(file.getPath());

            if(null == file) {
                log.warn("出现为空的File对象。");
                return false;
            }

            // 目录不能过滤掉，否则将不会监听到目录下的文件了。
            return file.isDirectory() || (!fileName.startsWith(".") && fileName.endsWith(".md"));
        }
    }

    /**
     * 对文件进行监听。
     */
    @Slf4j
    public static class MarkdownAlterationListener implements FileAlterationListener {
        private AtomicInteger listenerCount;

        public MarkdownAlterationListener(final AtomicInteger listenerCount) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFileChange(File file) {
            try {
                log.info(String.format("file: %s is changed", file.getCanonicalPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFileDelete(File file) {
            try {
                log.info(String.format("file: %s is deleted", file.getCanonicalPath()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onStop(FileAlterationObserver observer) {
            listenerCount.decrementAndGet();
            log.debug(String.format("listener stop"));
        }
    }
}
