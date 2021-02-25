package com.peter.solo.markdown.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
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
        Map obj = (Map) yaml.load(meta);

        if(null == obj) {
            log.info("文件（{}）：解析yaml信息失败。yaml原信息如下：\n{}", file.getName(), meta);
            return;
        }

        if(obj.containsKey("id") && !StringUtils.isEmpty(obj.get("id"))) {
            if(obj.get("id") instanceof Number) {
                blogMetaInfo.setId(String.valueOf(obj.get("id")));
            }
            else if(obj.get("id") instanceof  String) {
                blogMetaInfo.setId((String) obj.get("id"));
            }
            else {
                throw new RuntimeException("元信息中的ID信息格式不合法：" + obj.get("id").getClass().getSimpleName());
            }
        }

        if(obj.containsKey("title") && !StringUtils.isEmpty(obj.get("title"))) {
            blogMetaInfo.setTitle((String) obj.get("title"));
        }

        if(obj.containsKey("description") && !StringUtils.isEmpty(obj.get("description"))) {
            blogMetaInfo.setDescription((String) obj.get("description"));
        }

        if(obj.containsKey("date") && !StringUtils.isEmpty(obj.get("date"))) {
            if(obj.get("date") instanceof Date) {
                blogMetaInfo.setDate(DateFormatUtils.format((Date) obj.get("date"), "yyyy-MM-dd HH:mm:ss"));
            }
            else if(obj.get("date") instanceof  String) {
                blogMetaInfo.setDate((String) obj.get("date"));
            }
            else {
                throw new RuntimeException("元信息中的ID信息格式不合法：" + obj.get("date").getClass().getSimpleName());
            }
        }

        if(obj.containsKey("tags") && !StringUtils.isEmpty(obj.get("tags"))) {
            String[] tags = ((String) obj.get("tags")).split(",");
            List<String> tagList = new ArrayList<>();

            for(String tag: tags) {
                tagList.add(tag.trim());
            }

            blogMetaInfo.setTags(tagList);
        }

        if(obj.containsKey("pwd") && !StringUtils.isEmpty(obj.get("pwd"))) {
            blogMetaInfo.setPassword((String) obj.get("pwd"));
        }
        else if(obj.containsKey("password") && !StringUtils.isEmpty(obj.get("password"))) {
            blogMetaInfo.setPassword((String) obj.get("password"));
        }

        if(obj.containsKey("Blog") && null != obj.get("Blog")) {
            blogMetaInfo.setBlog((Boolean) obj.get("Blog"));
        }
        else if(obj.containsKey("blog") && null != obj.get("blog")) {
            blogMetaInfo.setBlog((Boolean) obj.get("blog"));
        }

        this.metaInfo = blogMetaInfo;
    }
}
