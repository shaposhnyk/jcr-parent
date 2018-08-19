package com.ljcr.srdb;

import com.ljcr.api.definitions.StandardType;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.srdb.mods.TypeDefinitionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

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
        RelationalTypeDefinition rootType = initSchema(res, rels);
        initData(rootType, res, rels);
    }

    private void initData(RelationalTypeDefinition rootType, ResourceRepository res, RelationRepository rels) {
        ObjectBuilder rootBuilder = newBuilder(res, rels, rootType);

        ObjectBuilder goldBuilder = rootBuilder.newFieldItemBuilder("colors")
                .setReference("gold")
                .setLocalized("displayName", "en", "Gold")
                .setLocalized("displayName", "fr", "Doré");

        ObjectBuilder coolBuilder = rootBuilder.newFieldItemBuilder("brands");

        coolBuilder
                .setReference("cool")
                .setLocalized("name", "en", "Cool Brand")
                .addItem(
                        coolBuilder.newFieldItemBuilder("shapes")
                                .setReference("round")
                )
                .addItem(
                        coolBuilder.newFieldItemBuilder("shapes")
                                .setReference("square")
                )
                .add("products",
                        coolBuilder.newFieldItemBuilder("products")
                                .setReference("CL12000")
                                .setLocalized("name", "en", "First product")
                                .setLocalized("name", "fr", "Premiere produit")
                                .set("stdPrice", new BigDecimal("999.95"))
                                .set("isNew", Boolean.TRUE)
                                .set("colorRef", "gold")
                );


        rootBuilder
                .addItem(goldBuilder)
                .addItem(
                        rootBuilder.newFieldItemBuilder("colors")
                                .setReference("silver")
                                .setLocalized("displayName", "en", "Silver")
                                .setLocalized("displayName", "fr", "Argenté")
                )
                .addItem(
                        rootBuilder.newFieldItemBuilder("brands")
                                .setReference("super")
                                .setLocalized("name", "en", "Super")
                )
                .addItem(coolBuilder)
                .build();

        showAll(res);
        showAll(rels);
    }

    private ObjectBuilder newBuilder(ResourceRepository res, RelationRepository rels, RelationalTypeDefinition rootType) {
        return new RelationalResourceBuilder(res, rels, rootType);
    }

    public RelationalTypeDefinition initSchema(ResourceRepository res, RelationRepository rels) {
        // save a couple of customers
        long count = res.count();
        log.info("count: {}", count);
        showAll(res);

        if (count < 1) {
            throw new IllegalStateException("Unknown state of the res");
        }

        // next objects must exist already in DB
        Resource tArray = res.findType(StandardTypes.ARRAY).get();
        Resource tMap = res.findType(StandardTypes.MAP).get();
        Resource typeDef = res.findType(StandardTypes.TYPEDEF).get();
        Resource repoRoot = res.findType(StandardTypes.REPOSITORY).get();

        res.save(TypeDefinitionBuilder.aliasOf(StandardTypes.STRING, "reference"));

        // base types should be already there
        RelationalTypeDefinition tLocString = newTypeDef("LocalizedString")
                .field("locale", StandardTypes.STRING, 5)
                .field("value", StandardTypes.STRING)
                .build(res, rels);

        RelationalTypeDefinition tColor = newTypeDef("Color")
                .field("displayName", tLocString)
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tShape = newTypeDef("Shape")
                .field("displayOrder", StandardTypes.LONG)
                .field("displayName", tLocString)
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tArticle = newTypeDef("Article")
                .field("name", tLocString)
                .field("description", tLocString)
                .field("stdPrice", StandardTypes.DECIMAL)
                .reference("shapeRef", tShape)
                .reference("colorRef", tColor)
                .field("isNew", StandardTypes.BOOLEAN)
                .isReferencable()
                .build(res, rels); // cross-referenced type

        RelationalTypeDefinition tBrand = newTypeDef("Brand")
                .field("name", tLocString)
                .referenceMap("shapes", tShape)
                .referenceMap("products", tArticle, "Retail articles")
                .referenceMap("components", tArticle, "Component articles")
                .isReferencable()
                .build(res, rels);

        RelationalTypeDefinition tBom = newTypeDef("BillOfMaterialLine")
                .reference("article", tArticle)
                .field("qty", StandardTypes.LONG)
                .field("role", StandardTypes.STRING)
                .build(res, rels);

        newTypeDef(tArticle)
                .reference("brandRef", tBrand)
                .repeatable("bom", tBom)
                .build(res, rels);


        RelationalTypeDefinition repo = newTypeDef("RepositoryType")
                .referenceMap("colors", tColor)
                .referenceMap("brands", tBrand)
                .build(res, rels);


        showAll(res);
        showAll(rels);

        return repo;
    }

    private TypeDefinitionBuilder newTypeDef(RelationalTypeDefinition tArticle) {
        return new TypeDefinitionBuilder(tArticle.getTypeResource());
    }

    private TypeDefinitionBuilder newTypeDef(String typeName) {
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

    private static Resource typeOf(StandardType type) {
        return TypeDefinitionBuilder.resourceOf(type);
    }
}