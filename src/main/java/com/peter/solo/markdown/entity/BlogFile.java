package com.peter.solo.markdown.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class BlogFile {
    private File file;
    private BlogMetaInfo metaInfo;
    private String content;

    public BlogFile(File file) {
        this.file = file;

        try {
            parseFile();
        } catch (IOException e) {
            log.error("文件解析失败", e);
        }
    }

    /**
     * 解析文件内容。获取文件元信息。
     * @throws IOException 文件读取失败
     */
    public void parseFile() throws IOException {
        BlogMetaInfo blogMetaInfo = new BlogMetaInfo();


        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
        StringBuilder metaBuilder = new StringBuilder();
        StringBuilder contentBuilder = new StringBuilder();
        int line = 0;
        boolean metaCollecting = false;

        try {
            while(reader.ready()) {
                line++;
                String lineContent = reader.readLine();

                if("---".equals(lineContent)) {
                    if (1 == line) {
                        metaCollecting = true;
                    }
                    else if(metaCollecting) {
                        metaCollecting = false;
                    }
                }
                else {
                    if(metaCollecting) {
                        metaBuilder.append(lineContent);
                        metaBuilder.append("\n");
                    }
                    else {
                        contentBuilder.append(lineContent);
                        contentBuilder.append("\n");
                    }
                }
            }
        }
        finally {
            reader.close();
        }

        content = contentBuilder.toString();

        if(metaBuilder.length() == 0) {
            log.warn("文件（{}）中没有找到yaml信息", file.getName());
            return;
        }

        // 解析文件头。
        String meta = metaBuilder.toString();
        Yaml yaml = new Yaml();
        Map<String, String> obj = (Map) yaml.load(meta);

        if(null == obj) {
            log.info("文件（{}）：解析yaml信息失败。yaml原信息如下：\n{}", file.getName(), meta);
            return;
        }

        if(obj.containsKey("id") && !StringUtils.isEmpty(obj.get("id"))) {
            blogMetaInfo.setId(obj.get("id"));
        }

        if(obj.containsKey("title") && !StringUtils.isEmpty(obj.get("title"))) {
            blogMetaInfo.setTitle(obj.get("title"));
        }

        if(obj.containsKey("date") && !StringUtils.isEmpty(obj.get("date"))) {
            blogMetaInfo.setDate(obj.get("date"));
        }

        if(obj.containsKey("tags") && !StringUtils.isEmpty(obj.get("tags"))) {
            String[] tags = obj.get("tags").split(",");
            List<String> tagList = new ArrayList<>();

            for(String tag: tags) {
                tagList.add(tag.trim());
            }

            blogMetaInfo.setTags(tagList);
        }

        if(obj.containsKey("pwd") && !StringUtils.isEmpty(obj.get("pwd"))) {
            blogMetaInfo.setPassword(obj.get("pwd"));
        }

        if(obj.containsKey("password") && !StringUtils.isEmpty(obj.get("password"))) {
            blogMetaInfo.setPassword(obj.get("password"));
        }

        if(obj.containsKey("blog") && !StringUtils.isEmpty(obj.get("blog"))) {
            blogMetaInfo.setBlog(Boolean.parseBoolean(obj.get("blog")));
        }

        this.metaInfo = blogMetaInfo;
    }
}
