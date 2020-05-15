package com.example.jwtdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.blockhound.BlockHound;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class JwtDemoApplication {

	public static void main(String[] args) {
		BlockHound.install();
		ReactorDebugAgent.init();
		SpringApplication.run(JwtDemoApplication.class, args);
	}

}
