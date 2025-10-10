package com.example.devSns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.devSns.domain.post.Post;
import com.example.devSns.domain.post.PostRepository;

@SpringBootApplication
public class DevSnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevSnsApplication.class, args);
	}

	// 실행 시 더미데이터 자동 삽입
	@Bean
	CommandLineRunner init(PostRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				repo.save(Post.builder()
						.content("hello h2")
						.username("hong")
						.likes(0)
						.build());
			}
		};
	}
}
