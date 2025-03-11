package com.quest.quest_web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.quest.etna")
@EntityScan(basePackages = "com.quest.etna.model")
@EnableJpaRepositories("com.quest.etna.repositories")
public class QuestWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuestWebApplication.class, args);
	}

}
