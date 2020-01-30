package com.peter.solo.markdown.input;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.http.impl.io.IdentityOutputStream;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 文件搜索
 */
@Slf4j
@Component
public class FileUpdate {
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
                        contentBuilder.append(lineContent);
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

    public static void main(String[] args) {
        File file = new File("/Users/yandong/Blogs/solo/note1.md");
        FileUpdate update = new FileUpdate();
        try {
            update.update("23453534534", file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
