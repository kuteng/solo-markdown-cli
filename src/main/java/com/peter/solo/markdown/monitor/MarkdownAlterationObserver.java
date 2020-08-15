package com.peter.solo.markdown.monitor;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Setter
@Component
public class MarkdownAlterationObserver extends FileAlterationObserver {
    /**
     * 是否正在checking。
     */
    private boolean checking = false;

    public MarkdownAlterationObserver(@Value("${solo.blog.root}") String directoryName) {
        super(directoryName, new MarkdownFileFilter());
    }

    @Override
    public void checkAndNotify() {
        if(checking) {
            log.warn("上次的检查未完成");
            return;
        }

        log.info("触发一次检查");

        try {
            this.checking = true;
            super.checkAndNotify();
        }
        finally {
            this.checking = false;
        }
    }
}
