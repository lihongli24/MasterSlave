package com.li.master.communication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Created by lihongli on 15/11/22.
 */

@SpringBootApplication
public class Application {

	public static void main(String[] args){
		SpringApplicationBuilder sb = new SpringApplicationBuilder(Application.class);
		sb.run(args);
	}
}
