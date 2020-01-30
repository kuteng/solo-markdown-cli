package com.peter.solo.markdown.controller;

import com.alibaba.fastjson.JSONObject;
import com.peter.solo.markdown.monitor.MarkdownAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
    @Autowired
    private MarkdownAlterationObserver observer;

    @GetMapping("/input")
    @ResponseBody
    public String input() {
        if(observer.isChecking()) {
            return "文件观察者正在检测文件状态";
        }

        observer.checkAndNotify();
        return "最新文件已上传成功。";
    }
}
