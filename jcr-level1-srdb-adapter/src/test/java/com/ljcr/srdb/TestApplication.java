package com.ljcr.srdb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestApplication {

    private static final Logger log = LoggerFactory.getLogger(TestApplication.class);

    @Autowired
    RelationRepository rels;
    @Autowired
    ResourceRepository res;
}
