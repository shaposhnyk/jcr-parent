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
@TestPropertySource(locations = "classpath:application-meta-02.properties")
@DataJpaTest

public class FieldDefTest {

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

        ImmutableNode fieldDef = reader.findResource("FieldDef", StandardTypes.TYPEDEF);
        assertThat(fieldDef, not(nullValue()));

        // type should be as before completely predefined :)
        assertThat(fieldDef.getTypeDefinition(), equalTo(StandardTypes.TYPEDEF));
        assertThat(fieldDef.getTypeDefinition().getPropertyDefinitions(), IsCollectionWithSize.hasSize(greaterThan(0)));
        assertThat(fieldDef.getTypeDefinition().getPropertyDefinitions().stream()
                        .map(p -> p.getIdentifier()).collect(toList()),
                hasItems("name", "doc", "fields"));

        ImmutableNode name = fieldDef.getItem("name");
        assertThat(name, not(nullValue()));
    }

}