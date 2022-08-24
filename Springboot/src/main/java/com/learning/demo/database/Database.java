package com.learning.demo.database;

import com.learning.demo.models.Product;
import com.learning.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
docker run -d --rm --name mysql-spring-boot-learning
-e MYSQL_ROOT_PASSWORD=123456
-e MYSQL_USER=haidv
-e MYSQL_PASSWORD=123456
-e MYSQL_DATABASE=test_db
-p 3309:3306
--volume mysql-spring-boot-learning-volume:/var/lib/mysql
mysql: latest

mysql -h localhost -P 3309 --protocol=tcp -u haidv -p
* */

@Configuration
public class Database {
    private static final Logger LOGGER = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product productA = new Product("Iphone 13", 2020, 2400.0, "");
                Product productB = new Product("Iphone 14", 2022, 2340.0, "");
//                productRepository.save(productA);
//                productRepository.save(productB);
                LOGGER.info("insert: " + productRepository.save(productA));
                LOGGER.info("insert: " + productRepository.save(productB));
            }
        };
    }
}
