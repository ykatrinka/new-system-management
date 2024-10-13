package ru.clevertec.commentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.clevertec.commentsservice.repository.impl.SearchRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
@EnableFeignClients
public class CommentsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentsServiceApplication.class, args);
    }
}
