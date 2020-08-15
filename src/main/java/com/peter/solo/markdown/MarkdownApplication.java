package com.peter.solo.markdown;

import com.peter.solo.markdown.monitor.FileMonitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class MarkdownApplication {
	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext ctx = SpringApplication.run(MarkdownApplication.class, args);
		FileMonitor oldFileMonitor = ctx.getBean(FileMonitor.class);
		oldFileMonitor.start();
	}
}
