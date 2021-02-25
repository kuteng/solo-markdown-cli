package com.peter.solo.markdown.input;

import com.peter.solo.markdown.entity.BlogFile;
import com.peter.solo.markdown.entity.BlogMetaInfo;
import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 文件搜索
 */
@Slf4j
@Component
public class FileUpdate {
    public void update(String id, BlogFile blogFile) throws IOException {
        String content = blogFile.getContent();
        BlogMetaInfo metaInfo = blogFile.getMetaInfo();
        File file = blogFile.getFile();

        if(null == metaInfo) {
            log.error("文件({})没有Meta信息，不应该上传成功。更不应该更新其元信息。", file.getName());
            return;
        }

        // 如果需要更新文件的ID，需要对文件执行写操作。
        if(StringUtils.isEmpty(metaInfo.getId()) || !id.equals(metaInfo.getId())) {
            if(!id.equals(metaInfo.getId())) {
                log.warn("文章的ID变了，old: " + metaInfo.getId() + ", new: " + id);
                metaInfo.setId(id);
            }

            log.info("开始更新文章ID: " + id + ", path: " + file.getCanonicalPath());

            String path = file.getCanonicalPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            try {
                writer.write("---\n");
                writer.write(printMeta(metaInfo));
                writer.write("---\n");
                writer.write(content);
                writer.flush();
                log.info("开始更新文章ID完毕: " + id + ", path: " + file.getCanonicalPath());
            }
            finally {
                writer.close();
            }
        }
    }

    public String printMeta(BlogMetaInfo metaInfo) {
        StringBuffer meta = new StringBuffer();

        if(!StringUtils.isEmpty(metaInfo.getId())) {
            if(StringUtils.isNumeric(metaInfo.getId())) {
                meta.append("id: ").append("'").append(metaInfo.getId()).append("'").append("\n");
            }
            else {
                meta.append("id: ").append(metaInfo.getId()).append("\n");
            }
        }

        meta.append("title: ").append(metaInfo.getTitle()).append("\n");
        meta.append("description: ").append(metaInfo.getDescription()).append("\n");
        meta.append("date: ").append(metaInfo.getDate()).append("\n");

        if(metaInfo.getTags() != null && metaInfo.getTags().size() > 0) {
            meta.append("tags: ").append(StringUtils.join(metaInfo.getTags(), ", ")).append("\n");
        }

        if(!StringUtils.isEmpty(metaInfo.getPassword())) {
            meta.append("password: ").append(metaInfo.getPassword()).append("\n");
        }

        if(!metaInfo.isBlog()) {
            meta.append("blog: ").append(metaInfo.isBlog()).append("\n");
        }

        return meta.toString();
    }
}
