package com.peter.solo.markdown.input;

import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    private PersonMapper personMapper;

    public void update(String id, File file) throws IOException {
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
                        contentBuilder.append(optPersonName(lineContent));
                        contentBuilder.append("\n");
                    }
                }
            }
        }
        finally {
            reader.close();
        }

        // 解析文件头。
        String content = contentBuilder.toString();
        String meta = metaBuilder.toString();
        Yaml yaml = new Yaml();
        Map obj = (Map) yaml.load(meta);

        // 如果需要更新文件的ID，需要对文件执行写操作。
        if(!obj.containsKey("id") || !id.equals(obj.get("id"))) {
            if(!id.equals(obj.get("id"))) {
                log.warn("文章的ID变了，old: " + obj.get("id") + ", new: " + id);
            }

            log.info("开始更新文章ID: " + id + ", path: " + file.getCanonicalPath());
            obj.put("id", id);

            if(obj.containsKey("date") && null != obj.get("date")) {
                if (obj.get("date") instanceof Date) {
                    obj.put("date", DateFormatUtils.format((Date) obj.get("date"), "yyyy-MM-dd HH:mm:ss"));
                }
                else if(obj.get("date") instanceof String) {
                    log.info("the date is String: " + obj.get("date"));
                    obj.put("date", (String) obj.get("date"));
                }
                else {
                    log.info("the date type is undefined: " + obj.get("date").toString());
                }
            }

            meta = yaml.dumpAsMap(obj);

            String path = file.getCanonicalPath();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            try {
                writer.write("---\n");
                writer.write(meta);
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

    /**
     * 处理每行中的人名。
     * todo by yandong: 当前算法可能有些耗时。
     * @param lineContent
     * @return
     */
    private String optPersonName(String lineContent) {
        List<Person> persons = personMapper.findAll();

        for(Person p: persons) {
            lineContent = lineContent.replace(p.getName(), p.getAlias());
        }

        return lineContent;
    }

    public static void main(String[] args) {
        String conent = "闫冬没有发现这个问题，但是房斌斌发现了";
        System.out.println(new FileUpdate().optPersonName(conent));
    }
}
