package com.peter.solo.markdown.controller;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.entity.BlogFile;
import com.peter.solo.markdown.entity.Result;
import com.peter.solo.markdown.input.InputService;
import com.peter.solo.markdown.monitor.MarkdownAlterationObserver;
import com.peter.solo.markdown.monitor.MarkdownFileFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("/blog")
public class FileController {
    @Autowired
    private MarkdownAlterationObserver observer;
    @Autowired
    private InputService inputService;
    @Value("${solo.blog.root}")
    private String rootPath;

    /**
     * 手动触发文章的检验和上传。
     */
    @GetMapping("/input")
    @ResponseBody
    public Result input() {
        if(observer.isChecking()) {
            String msg = "文件观察者正在检测文件状态";
            log.error(msg);
            return new Result(Result.STATUS_FAILURE, msg);
        }

        log.debug(">>>> 测试 <<<<<");
        observer.checkAndNotify();
        return new Result(Result.STATUS_SUCESS, "最新文件已上传成功。");
    }

    /**
     * 手动触发，将全部文章上传到博客。
     */
    @GetMapping("/input-all")
    @ResponseBody
    public Result inputAll() {
        File rootFolder = new File(rootPath);

        if(!rootFolder.exists()) {
            String msg = String.format("文章根目录不存在：%s", rootPath);
            log.error(msg);
            return new Result(Result.STATUS_FAILURE, msg);
        }

        if(!rootFolder.isDirectory()) {
            String msg = String.format("RootPath不是目录：%s", rootPath);
            log.error(msg);
            return new Result(Result.STATUS_FAILURE, msg);
        }

        try {
            inputFiles(rootFolder, new MarkdownFileFilter());
        }
        catch (IOException e) {
            log.error("文件上传失败", e);
            return new Result(Result.STATUS_SUCESS, e.getMessage());
        }

        return new Result(Result.STATUS_SUCESS, "最新文件已上传成功。");
    }

    private void inputFiles(File file, MarkdownFileFilter fileFilter) throws IOException {
        if(null == file) {
            return;
        }

        if(file.isDirectory()) {
            for(File child: file.listFiles(fileFilter)) {
                inputFiles(child, fileFilter);
            }
        }
        else {
            inputService.updateFile(new BlogFile(file));
        }
    }
}
