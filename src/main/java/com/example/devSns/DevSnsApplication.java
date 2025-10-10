package com.example.devSns;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevSnsApplication{

	public static void main(String[] args) {
		// 1. .env 파일 로드
		Dotenv dotenv = Dotenv.load();

		// 2. 환경 변수를 시스템 속성으로 설정하여 스프링이 읽을 수 있게 함
		dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(DevSnsApplication.class, args);
	}

}
