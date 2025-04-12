package com.music;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("com.music.repository")
@EntityScan("com.music.model")
@EnableJpaAuditing
public class SpringMusicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMusicApplication.class, args);
    }
}
