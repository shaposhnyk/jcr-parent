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
    public CommandLineRunner demo(ResourceRepository res, RelationRepository rels) {
        return (args) -> demo(res, rels, args);
    }

    public void demo(ResourceRepository res, RelationRepository rels, String... args) {
        // save a couple of customers
        long count = res.count();
        log.info("count: {}", count);
        showAll(res);

        if (count < 1) {
            throw new IllegalStateException("Unknown state of the res");
        }

        Resource typeDef = res
                .findById(StandardTypes.TYPEDEF.getNumericCode() * 1L)
                .get();

        // base types should be already there
        RelationalTypeDefinition tLocString = typeDef("LocalizedString")
                .field("locale", StandardTypes.STRING, 5)
                .field("value", StandardTypes.STRING)
                .build(res, rels);

        res.save(TypeDefinitionBuilder.typeOf(StandardTypes.STRING, "reference"));

        RelationalTypeDefinition tColor = typeDef("Color")
                .field("displayName", tLocString)
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tShape = typeDef("Shape")
                .field("displayOrder", StandardTypes.LONG)
                .field("displayName", tLocString)
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tArticle = typeDef("Article")
                .build(res, rels); // cross-referenced type

        RelationalTypeDefinition tArticle2 = typeDef("Article2")
                .build(res, rels); // cross-referenced type

        RelationalTypeDefinition tBrand = typeDef("Brand")
                .field("name", tLocString)
                .referenceMap("shapes", tShape)
                .referenceMap("products", tArticle, "Retail articles")
                .referenceMap("components", tArticle, "Component articles")
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tBom = typeDef("BillOfMaterialLine")
                .reference("article", tArticle)
                .field("qty", StandardTypes.LONG)
                .field("role", StandardTypes.STRING)
                .build(res, rels);

        typeDef(tArticle)
                .field("name", tLocString)
                .field("description", tLocString)
                .field("stdPrice", StandardTypes.DECIMAL)
                .reference("brandRef", tBrand)
                .reference("shapeRef", tShape)
                .reference("colorRef", tColor)
                .repeatable("bom", tBom)
                .isReferencable()
                .build(res, rels);

        showAll(res);
        showAll(rels);
    }

    private TypeDefinitionBuilder typeDef(RelationalTypeDefinition tArticle) {
        return new TypeDefinitionBuilder(tArticle.getIdentifier());
    }

    private TypeDefinitionBuilder typeDef(String typeName) {
        return new TypeDefinitionBuilder(typeName);
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

    private static Resource typeOf(StandardTypes.StandardScalar type) {
        return TypeDefinitionBuilder.typeOf(type);
    }
}