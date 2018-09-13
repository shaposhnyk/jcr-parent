package com.ljcr.srdb;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.StandardTypes;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(classes = TestApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-types-03.properties")
@DataJpaTest

public class SimpleTypesTest {

    @Autowired
    RelationRepository rels;
    @Autowired
    ResourceRepository res;

    @Test
    public void immutableResourceChecks() {
        RepositoryReader reader = new RepositoryReader(res, rels);
        assertThat(res, not(nullValue()));
        assertThat(rels, not(nullValue()));
        assertThat(reader, not(nullValue()));

        ImmutableNode def = reader.findResource("LocalizedString", StandardTypes.TYPEDEF);
        assertThat(def, not(nullValue()));

        // type should be meta def
        assertThat(def.getTypeDefinition(), equalTo(StandardTypes.TYPEDEF));
        assertThat(def.getTypeDefinition().getPropertyDefinitions(), IsCollectionWithSize.hasSize(greaterThan(0)));
        assertThat(def.getTypeDefinition().getPropertyDefinitions().stream()
                        .map(p -> p.getIdentifier()).collect(toList()),
                hasItems("name", "doc", "fields"));

        ImmutableNode name = def.getItem("name");
        assertThat(name, not(nullValue()));
        assertThat(name.asString(), equalTo("LocalizedString"));

        ImmutableNode fieldsNode = def.getItem("fields");
        assertThat(fieldsNode, not(nullValue()));
        assertThat(fieldsNode.getValue(), not(nullValue()));
    }

}