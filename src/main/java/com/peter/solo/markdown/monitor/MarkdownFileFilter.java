package com.peter.solo.markdown.monitor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * 文件过滤<br/>
 * 只有文件夹、不是以"."开头的".md"文件才能通过过滤。
 */
@Slf4j
public class MarkdownFileFilter implements FileFilter {
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
