package com.ljcr.srdb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource(locations = "classpath:application-meta-01.properties")
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = ResourceRepository.class)
public class RdbAdapterTest {

    @Autowired
    RelationRepository rels;
    @Autowired
    ResourceRepository res;

    @Test
    public void test() {
        RepositoryReader reader = new RepositoryReader(res, rels);
        assertThat(res, not(nullValue()));
        assertThat(reader, not(nullValue()));
    }

}