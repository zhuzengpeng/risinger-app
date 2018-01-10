package com.bbd.risinger;

import com.bbd.risinger.modules.sys.service.SystemService;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring boot启动类
 * @author zzp
 */
@EnableCaching
@SpringBootApplication
@ServletComponentScan("com.bbd.risinger")
@ComponentScan(value = "com.bbd.risinger" ,lazyInit = true)
public class RisingerApplication {
	
    public static void main(String[] args) {
    	SpringApplication.run(RisingerApplication.class, args);
		SystemService.printKeyLoadMessage();
    }

}
