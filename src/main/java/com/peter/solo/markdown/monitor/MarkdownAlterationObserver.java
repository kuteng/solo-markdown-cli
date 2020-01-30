package com.peter.solo.markdown.monitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileFilter;

@Service
@Slf4j
public class MarkdownAlterationObserver extends FileAlterationObserver {
    public MarkdownAlterationObserver(@Value("${solo.blog.root}") String directoryName) {
        super(directoryName, new MarkdownFileFilter());
    }

    @Override
    public void checkAndNotify() {
        super.checkAndNotify();
    }

    public void test() {
        log.info(">>> observer.test()");
    }
}
