package com.ljcr.srdb;

import com.ljcr.api.definitions.StandardTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(ResourceRepository repository, RelationRepository rels) {
        return (args) -> demo(repository, rels, args);
    }

    public void demo(ResourceRepository repository, RelationRepository rels, String... args) {
        // save a couple of customers
        log.info("count: {}", repository.count());

        showAll(repository);
/*
        Resource typeDef = repository.findById(StandardTypes.TYPEDEF.getNumericCode() * 1L)
                .get();

        repository.save(typeOf(StandardTypes.STRING, typeDef));
        repository.save(typeOf(StandardTypes.LONG, typeDef));
        repository.save(typeOf(StandardTypes.DECIMAL, typeDef));
        repository.save(typeOf(StandardTypes.REFERENCE, typeDef));
        repository.save(typeOf(StandardTypes.REPOSITORY, typeDef));
*/
    }

    private <T> void showAll(CrudRepository<T, ?> repository) {
        // fetch all customers
        log.info("Resources found with findAll():");
        log.info("-------------------------------[");
        for (T customer : repository.findAll()) {
            log.info("- {}", customer.toString());
        }
        log.info("-------------------------------]");
    }

    private Resource typeOf(StandardTypes.StandardScalar type, Resource typeDef) {
        return new Resource(1L * type.getNumericCode(), typeDef, type.getIdentifier());
    }
}