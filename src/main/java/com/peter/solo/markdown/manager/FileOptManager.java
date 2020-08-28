package com.peter.solo.markdown.manager;

import com.peter.solo.markdown.entity.Person;
import com.peter.solo.markdown.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * 对文件进行处理<br/>
 * 1. 将文件中的人名进行替换。
 * @Author yandong.great
 * @Date 2020/8/27 12:23 上午
 */
@Slf4j
@Component
public class FileOptManager {
    @Autowired
    private PersonManager personManager;
    @Value("${solo.blog.temp}")
    private String tempPath;

    public File optFile(File file) throws IOException {
        File tmpFile = temp(file);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
        String line;
        boolean metaCollecting = false;
        int lineNum = 0;

        while((line = reader.readLine()) != null) {
            if(Pattern.compile("^-{3}$").matcher(line).matches() && (lineNum == 0 || metaCollecting)) {
                metaCollecting = !metaCollecting;
            }
            else if(!metaCollecting) {
                line = optPersonName(line);
            }

            writer.append(line).append("\n");
            lineNum++;
        }

        reader.close();
        writer.close();
        return tmpFile;
    }

    private File temp(File file) throws IOException {
        File tempFolder = new File(tempPath);

        if(!tempFolder.exists()) {
            tempFolder.mkdirs();
        }

        String originFileName = file.getName();

        File tempFile = File.createTempFile(originFileName.substring(0, originFileName.lastIndexOf(".")),
                originFileName.substring(originFileName.lastIndexOf(".")),
                tempFolder);

        return tempFile;
    }

    /**
     * 处理每行中的人名。
     * todo by yandong: 当前算法可能有些低效。
     * @param lineContent
     * @return
     */
    private String optPersonName(String lineContent) {
        List<Person> persons = personManager.getPersons();

        for (Person p : persons) {
            lineContent = lineContent.replace(p.getName(), p.getAlias());
        }

        return lineContent;
    }
}