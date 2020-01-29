package com.peter.solo.markdown.input;

import com.peter.solo.markdown.FileMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InputService {
    @Autowired
    private FileMonitor fileMonitor;


    public void run() {
    }
}
