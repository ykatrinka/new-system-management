package ru.clevertec.newsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.clevertec.newsservice.repository.impl.SearchRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = SearchRepositoryImpl.class)
@EnableFeignClients
public class NewsServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NewsServiceApplication.class, args);
    }
}

