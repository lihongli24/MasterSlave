package com.li.slave.communication;

import com.li.slave.communication.config.WebConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;

/**
 * Created by lihongli on 15/11/22.
 */
@SpringBootApplication
@Import({WebConfig.class})
public class Application {

	/**
	 * baseDir = args[0];
	 serverIp = args[1];
	 serverPort = Integer.parseInt(args[2]);
	 * @param args
	 */
	public static String slaveIp = null;
	public static Integer slavePort = null;


	public static void main(String[] args) {

		SpringApplicationBuilder sb = new SpringApplicationBuilder(Application.class);

		slaveIp = args[0];
		slavePort = Integer.parseInt(args[1]);
		sb.run(args);
	}

}
